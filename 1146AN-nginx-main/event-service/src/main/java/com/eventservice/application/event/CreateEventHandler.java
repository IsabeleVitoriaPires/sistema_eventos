package com.eventservice.application.event;

import com.eventservice.domain.event.Event;
import com.eventservice.domain.event.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case: Create a new event
 * Requisito 5: Cadastro de eventos por organizadores
 * Applies Factory Pattern for event creation
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CreateEventHandler {

    private final EventRepository eventRepository;

    /**
     * Execute: Create a new event
     * @param request Create event request DTO
     * @return Created event DTO
     * @throws IllegalArgumentException if validation fails
     */
    @Transactional
    public EventResponseDTO execute(CreateEventRequestDTO request) {
        log.info("Creating new event: {}", request.getName());

        // Factory Pattern: Convert DTO to domain entity
        Event event = request.toDomain();

        // Validate business rules
        event.validate();

        // Save event
        Event savedEvent = eventRepository.save(event);

        log.info("Event created successfully with ID: {}", savedEvent.getId());

        return EventResponseDTO.fromDomain(savedEvent);
    }
}
