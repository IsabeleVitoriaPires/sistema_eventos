package com.eventservice.infrastructure.event.controller;

import com.eventservice.application.event.EventWithMetricsDTO;
import com.eventservice.application.event.GetOrganizerDashboardHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Dashboard REST Controller
 * Requisito 6: Dashboard do organizador com metricas
 *
 * Requires ORGANIZER role (enforced by gateway)
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final GetOrganizerDashboardHandler getOrganizerDashboardHandler;

    /**
     * Requisito 6: Get organizer dashboard with all events and sales metrics
     * GET /api/dashboard/organizer/{organizerId}
     *
     * Returns list of events with:
     * - Event details (name, date, location, price)
     * - Total tickets sold
     * - Total revenue
     * - Tickets remaining
     * - Occupancy rate (%)
     */
    @GetMapping("/organizer/{organizerId}")
    public ResponseEntity<List<EventWithMetricsDTO>> getOrganizerDashboard(
            @PathVariable UUID organizerId) {
        List<EventWithMetricsDTO> dashboard = getOrganizerDashboardHandler.execute(organizerId);
        return ResponseEntity.ok(dashboard);
    }
}
