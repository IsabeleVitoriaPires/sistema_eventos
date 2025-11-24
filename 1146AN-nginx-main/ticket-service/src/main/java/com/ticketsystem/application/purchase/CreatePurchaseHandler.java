package com.ticketsystem.application.purchase;

import com.ticketsystem.domain.purchase.Purchase;
import com.ticketsystem.domain.purchase.PurchaseRepository;
import com.ticketsystem.domain.purchase.PurchaseStatus;
import com.ticketsystem.domain.ticket.Ticket;
import com.ticketsystem.domain.ticket.TicketRepository;
import com.ticketsystem.domain.ticket.TicketStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Use Case: Create a new purchase with multiple tickets
 * Requisito 4: Compra de ingressos com informacoes dos participantes
 *
 * Business flow:
 * 1. Fetch event from event-service
 * 2. Validate ticket availability
 * 3. Calculate total amount (unit price x quantity)
 * 4. Create purchase
 * 5. Create individual tickets with attendee information
 * 6. Reserve tickets in event-service
 * 7. Confirm purchase
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CreatePurchaseHandler {

    private final PurchaseRepository purchaseRepository;
    private final TicketRepository ticketRepository;
    private final EventServiceClient eventServiceClient;

    @Transactional
    public PurchaseResponseDTO execute(CreatePurchaseRequestDTO request) {
        log.info("Creating purchase for user {} and event {}", request.getUserId(), request.getEventId());

        // Step 1: Fetch event from event-service
        EventDTO event = eventServiceClient.getEvent(request.getEventId());
        log.info("Event found: {} - Price: {}", event.getName(), event.getTicketPrice());

        // Step 2: Validate ticket availability
        int requestedQuantity = request.getQuantity();
        if (!event.hasAvailableTickets(requestedQuantity)) {
            log.error("Insufficient tickets. Requested: {}, Available: {}",
                    requestedQuantity, event.getAvailableTickets());
            throw new InsufficientTicketsException(
                    String.format("Tickets insuficientes. Solicitado: %d, Disponivel: %d",
                            requestedQuantity, event.getAvailableTickets())
            );
        }

        // Step 3: Create purchase
        Purchase purchase = Purchase.builder()
                .id(UUID.randomUUID())
                .userId(request.getUserId())
                .eventId(request.getEventId())
                .quantity(requestedQuantity)
                .status(PurchaseStatus.PENDING)
                .purchaseCode(Purchase.generatePurchaseCode())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .tickets(new ArrayList<>())
                .build();

        // Step 4: Calculate total amount (Requisito 4.2)
        BigDecimal ticketPrice = event.getTicketPrice();
        purchase.calculateTotalAmount(ticketPrice);
        log.info("Total amount calculated: {} (price: {} x quantity: {})",
                purchase.getTotalAmount(), ticketPrice, requestedQuantity);

        // Step 5: Create individual tickets with attendee information (Requisito 4.3)
        List<Ticket> tickets = new ArrayList<>();
        for (AttendeeDTO attendeeDTO : request.getAttendees()) {
            Ticket ticket = Ticket.builder()
                    .id(UUID.randomUUID())
                    .purchaseId(purchase.getId())
                    .eventId(request.getEventId())
                    .attendee(attendeeDTO.toDomain())
                    .price(ticketPrice)
                    .status(TicketStatus.ACTIVE)
                    .ticketCode(Ticket.generateTicketCode())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            ticket.validate();
            tickets.add(ticket);
            purchase.addTicket(ticket);
        }

        // Validate purchase
        purchase.validate();

        // Step 6: Save purchase and tickets
        Purchase savedPurchase = purchaseRepository.save(purchase);
        ticketRepository.saveAll(tickets);
        log.info("Purchase saved with {} tickets. Purchase code: {}",
                tickets.size(), savedPurchase.getPurchaseCode());

        // Step 7: Reserve tickets in event-service
        try {
            eventServiceClient.reserveTickets(request.getEventId(), requestedQuantity);
            log.info("Tickets reserved successfully in event-service");
        } catch (Exception e) {
            log.error("Failed to reserve tickets in event-service: {}", e.getMessage());
            // In a real system, we would implement compensating transaction here
            // (rollback the purchase) or use SAGA pattern
        }

        // Step 8: Confirm purchase
        savedPurchase.confirm();
        savedPurchase = purchaseRepository.save(savedPurchase);
        log.info("Purchase confirmed: {}", savedPurchase.getPurchaseCode());

        return PurchaseResponseDTO.fromDomain(savedPurchase);
    }
}
