package com.eventservice.application.event;

import com.eventservice.domain.event.Event;
import com.eventservice.domain.event.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Use Case: Update an existing event
 * Only updates non-null fields (partial update)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateEventHandler {

    private final EventRepository eventRepository;

    /**
     * Execute: Update event
     * @param eventId Event UUID
     * @param request Update request DTO
     * @return Updated event DTO
     * @throws EventNotFoundException if event not found
     */
    @Transactional
    public EventResponseDTO execute(UUID eventId, UpdateEventRequestDTO request) {
        log.info("Updating event with ID: {}", eventId);

        // Find existing event
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + eventId));

        // Update only non-null fields
        if (request.getName() != null) {
            event.setName(request.getName());
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (request.getEventDate() != null) {
            event.setEventDate(request.getEventDate());
        }
        if (request.getLocation() != null) {
            event.setLocation(request.getLocation());
        }
        if (request.getTicketPrice() != null) {
            event.setTicketPrice(request.getTicketPrice());
        }
        if (request.getTotalTickets() != null) {
            // Adjust available tickets if total tickets changed
            int difference = request.getTotalTickets() - event.getTotalTickets();
            event.setTotalTickets(request.getTotalTickets());
            event.setAvailableTickets(event.getAvailableTickets() + difference);
        }

        event.setUpdatedAt(LocalDateTime.now());

        // Validate updated event
        event.validate();

        // Save changes
        Event updatedEvent = eventRepository.save(event);

        log.info("Event updated successfully: {}", eventId);

        return EventResponseDTO.fromDomain(updatedEvent);
    }
}
