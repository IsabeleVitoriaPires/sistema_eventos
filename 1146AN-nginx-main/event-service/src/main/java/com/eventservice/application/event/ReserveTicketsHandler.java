package com.eventservice.application.event;

import com.eventservice.domain.event.Event;
import com.eventservice.domain.event.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use Case: Reserve tickets for an event
 * Decrements available tickets when a purchase is made
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReserveTicketsHandler {

    private final EventRepository eventRepository;

    /**
     * Execute: Reserve tickets for an event
     * @param eventId The ID of the event
     * @param quantity The number of tickets to reserve
     * @return Updated event DTO
     * @throws EventNotFoundException if event not found
     * @throws com.eventservice.domain.event.InsufficientTicketsException if not enough tickets available
     */
    @Transactional
    public EventResponseDTO execute(UUID eventId, int quantity) {
        log.info("Reserving {} tickets for event {}", quantity, eventId);

        // Find the event
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + eventId));

        // Reserve tickets (business logic in domain entity)
        event.reserveTickets(quantity);

        // Save updated event
        Event updatedEvent = eventRepository.save(event);

        log.info("Successfully reserved {} tickets for event {}. Available tickets: {}",
                quantity, eventId, updatedEvent.getAvailableTickets());

        return EventResponseDTO.fromDomain(updatedEvent);
    }
}
