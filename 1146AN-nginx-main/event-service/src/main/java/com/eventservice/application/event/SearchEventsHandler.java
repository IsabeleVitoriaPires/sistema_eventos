package com.eventservice.application.event;

import com.eventservice.domain.event.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Use Case: Search events by name
 * Requisito 7: Pesquisa de eventos
 */
@Service
@RequiredArgsConstructor
public class SearchEventsHandler {

    private final EventRepository eventRepository;

    /**
     * Execute: Search events by name (case insensitive)
     * @param searchTerm Search term for event name
     * @return List of matching event DTOs
     */
    public List<EventResponseDTO> execute(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return eventRepository.findAll()
                    .stream()
                    .map(EventResponseDTO::fromDomain)
                    .collect(Collectors.toList());
        }

        return eventRepository.findByNameContainingIgnoreCase(searchTerm)
                .stream()
                .map(EventResponseDTO::fromDomain)
                .collect(Collectors.toList());
    }
}
