package com.ticketsystem.infrastructure.purchase.controller;

import com.ticketsystem.application.purchase.EventMetricsDTO;
import com.ticketsystem.application.purchase.GetEventMetricsHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for Sales Metrics
 * Requisito 6: Dashboard do organizador
 *
 * Endpoints require ORGANIZER role (enforced by gateway)
 */
@RestController
@RequestMapping("/api/metrics")
@RequiredArgsConstructor
public class MetricsController {

    private final GetEventMetricsHandler getEventMetricsHandler;

    /**
     * Requisito 6: Get sales metrics for a specific event
     * GET /api/metrics/events/{eventId}
     *
     * Returns:
     * - Total tickets sold
     * - Total revenue
     * - Total purchases
     */
    @GetMapping("/events/{eventId}")
    public ResponseEntity<EventMetricsDTO> getEventMetrics(@PathVariable UUID eventId) {
        EventMetricsDTO metrics = getEventMetricsHandler.execute(eventId);
        return ResponseEntity.ok(metrics);
    }
}
