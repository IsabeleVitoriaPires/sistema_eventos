package com.eventservice.infrastructure.event.controller;

import com.eventservice.application.event.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Public REST Controller for Events
 * Requisitos 1, 2 e 7: Visualização pública e pesquisa de eventos
 * No authentication required - accessible to all visitors
 */
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class PublicEventController {

    private final ListEventsHandler listEventsHandler;
    private final GetEventByIdHandler getEventByIdHandler;
    private final SearchEventsHandler searchEventsHandler;
    private final ReserveTicketsHandler reserveTicketsHandler;

    /**
     * Requisito 1: List all events (public access)
     * GET /api/events
     */
    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> listAllEvents() {
        List<EventResponseDTO> events = listEventsHandler.execute();
        return ResponseEntity.ok(events);
    }

    /**
     * Requisito 2: Get event details by ID (public access)
     * GET /api/events/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable UUID id) {
        EventResponseDTO event = getEventByIdHandler.execute(id);
        return ResponseEntity.ok(event);
    }

    /**
     * Requisito 7: Search events by name (public access)
     * GET /api/events/search?q=searchTerm
     */
    @GetMapping("/search")
    public ResponseEntity<List<EventResponseDTO>> searchEvents(
            @RequestParam(value = "q", required = false) String searchTerm) {
        List<EventResponseDTO> events = searchEventsHandler.execute(searchTerm);
        return ResponseEntity.ok(events);
    }

    /**
     * Reserve tickets for an event (public access)
     * PATCH /api/events/{id}/reserve
     * Called by ticket-service when a purchase is made
     */
    @PatchMapping("/{id}/reserve")
    public ResponseEntity<EventResponseDTO> reserveTickets(
            @PathVariable UUID id,
            @RequestBody ReserveTicketsRequestDTO request) {
        EventResponseDTO event = reserveTicketsHandler.execute(id, request.getQuantity());
        return ResponseEntity.ok(event);
    }
}
