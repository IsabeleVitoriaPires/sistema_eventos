package com.eventservice.application.event;

import com.eventservice.domain.event.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Use Case: Get events by organizer
 * Requisito 6: Dashboard do organizador
 */
@Service
@RequiredArgsConstructor
public class GetEventsByOrganizerHandler {

    private final EventRepository eventRepository;

    /**
     * Execute: Find all events for a specific organizer
     * @param organizerId Organizer UUID
     * @return List of event DTOs
     */
    public List<EventResponseDTO> execute(UUID organizerId) {
        return eventRepository.findByOrganizerId(organizerId)
                .stream()
                .map(EventResponseDTO::fromDomain)
                .collect(Collectors.toList());
    }
}
