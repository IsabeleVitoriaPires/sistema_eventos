package com.eventservice.application.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO for Event Metrics from ticket-service
 * Used for service-to-service communication
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventMetricsDTO {
    private UUID eventId;
    private Long totalTicketsSold;
    private BigDecimal totalRevenue;
    private Long totalPurchases;
}
