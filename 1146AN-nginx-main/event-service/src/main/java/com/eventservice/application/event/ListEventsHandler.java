package com.eventservice.application.event;

import com.eventservice.domain.event.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Use Case: List all events
 * Requisito 1 e 2: Visualização pública de eventos
 */
@Service
@RequiredArgsConstructor
public class ListEventsHandler {

    private final EventRepository eventRepository;

    /**
     * Execute: List all events
     * @return List of event DTOs
     */
    public List<EventResponseDTO> execute() {
        return eventRepository.findAll()
                .stream()
                .map(EventResponseDTO::fromDomain)
                .collect(Collectors.toList());
    }
}
