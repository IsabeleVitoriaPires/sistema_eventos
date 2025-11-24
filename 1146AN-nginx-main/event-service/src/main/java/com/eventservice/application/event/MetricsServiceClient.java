package com.eventservice.application.event;

import java.util.UUID;

/**
 * Port (Interface) for Metrics Service communication
 * Implementation will be in infrastructure layer
 */
public interface MetricsServiceClient {
    /**
     * Get sales metrics for an event from ticket-service
     * @param eventId Event UUID
     * @return Metrics DTO
     */
    EventMetricsDTO getEventMetrics(UUID eventId);
}
