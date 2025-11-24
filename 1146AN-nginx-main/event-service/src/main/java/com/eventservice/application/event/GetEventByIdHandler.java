package com.eventservice.application.event;

import com.eventservice.domain.event.Event;
import com.eventservice.domain.event.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Use Case: Get event by ID
 * Requisito 2: Visualização de detalhes do evento
 */
@Service
@RequiredArgsConstructor
public class GetEventByIdHandler {

    private final EventRepository eventRepository;

    /**
     * Execute: Find event by ID
     * @param eventId Event UUID
     * @return Event DTO
     * @throws EventNotFoundException if event not found
     */
    public EventResponseDTO execute(UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + eventId));

        return EventResponseDTO.fromDomain(event);
    }
}
