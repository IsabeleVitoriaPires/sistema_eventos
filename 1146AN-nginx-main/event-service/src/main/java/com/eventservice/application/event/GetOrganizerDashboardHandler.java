package com.eventservice.application.event;

import com.eventservice.domain.event.Event;
import com.eventservice.domain.event.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Use Case: Get organizer dashboard with all events and metrics
 * Requisito 6: Dashboard do organizador
 *
 * Combines event data from event-service with sales metrics from ticket-service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GetOrganizerDashboardHandler {

    private final EventRepository eventRepository;
    private final MetricsServiceClient metricsServiceClient;

    public List<EventWithMetricsDTO> execute(UUID organizerId) {
        log.info("Fetching dashboard for organizer: {}", organizerId);

        // Get all events for organizer
        List<Event> events = eventRepository.findByOrganizerId(organizerId);

        log.info("Found {} events for organizer {}", events.size(), organizerId);

        // For each event, fetch metrics and combine
        return events.stream()
                .map(event -> {
                    EventMetricsDTO metrics = metricsServiceClient.getEventMetrics(event.getId());
                    return buildEventWithMetrics(event, metrics);
                })
                .collect(Collectors.toList());
    }

    private EventWithMetricsDTO buildEventWithMetrics(Event event, EventMetricsDTO metrics) {
        // Calculate tickets remaining
        Integer ticketsRemaining = event.getAvailableTickets();

        // Calculate occupancy rate (percentage of tickets sold)
        BigDecimal occupancyRate = BigDecimal.ZERO;
        if (event.getTotalTickets() != null && event.getTotalTickets() > 0) {
            long ticketsSold = metrics.getTotalTicketsSold() != null ? metrics.getTotalTicketsSold() : 0L;
            occupancyRate = BigDecimal.valueOf(ticketsSold)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(event.getTotalTickets()), 2, RoundingMode.HALF_UP);
        }

        return EventWithMetricsDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .location(event.getLocation())
                .ticketPrice(event.getTicketPrice())
                .totalTickets(event.getTotalTickets())
                .availableTickets(event.getAvailableTickets())
                .ticketsSold(metrics.getTotalTicketsSold())
                .totalRevenue(metrics.getTotalRevenue())
                .totalPurchases(metrics.getTotalPurchases())
                .ticketsRemaining(ticketsRemaining)
                .occupancyRate(occupancyRate)
                .build();
    }
}
