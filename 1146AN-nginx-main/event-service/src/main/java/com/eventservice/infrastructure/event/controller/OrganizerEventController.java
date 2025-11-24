package com.eventservice.infrastructure.event.controller;

import com.eventservice.application.event.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Organizer REST Controller for Events
 * Requisito 5: Cadastro de eventos por organizadores
 * Requires ORGANIZER role (enforced by gateway)
 */
@RestController
@RequestMapping("/api/organizer/events")
@RequiredArgsConstructor
public class OrganizerEventController {

    private final CreateEventHandler createEventHandler;
    private final UpdateEventHandler updateEventHandler;
    private final DeleteEventHandler deleteEventHandler;
    private final GetEventsByOrganizerHandler getEventsByOrganizerHandler;

    /**
     * Requisito 5: Create a new event
     * POST /api/organizer/events
     */
    @PostMapping
    public ResponseEntity<EventResponseDTO> createEvent(
            @Valid @RequestBody CreateEventRequestDTO request) {
        EventResponseDTO event = createEventHandler.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }

    /**
     * Update an existing event
     * PUT /api/organizer/events/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDTO> updateEvent(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateEventRequestDTO request) {
        EventResponseDTO event = updateEventHandler.execute(id, request);
        return ResponseEntity.ok(event);
    }

    /**
     * Delete an event
     * DELETE /api/organizer/events/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID id) {
        deleteEventHandler.execute(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get all events for a specific organizer
     * GET /api/organizer/events/my-events/{organizerId}
     */
    @GetMapping("/my-events/{organizerId}")
    public ResponseEntity<List<EventResponseDTO>> getMyEvents(@PathVariable UUID organizerId) {
        List<EventResponseDTO> events = getEventsByOrganizerHandler.execute(organizerId);
        return ResponseEntity.ok(events);
    }
}
