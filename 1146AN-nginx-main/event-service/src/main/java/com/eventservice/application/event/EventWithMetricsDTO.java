package com.eventservice.application.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO combining Event data with Sales Metrics
 * Requisito 6: Dashboard do organizador
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventWithMetricsDTO {
    // Event data
    private UUID id;
    private String name;
    private String description;
    private LocalDateTime eventDate;
    private String location;
    private BigDecimal ticketPrice;
    private Integer totalTickets;
    private Integer availableTickets;

    // Metrics data (from ticket-service)
    private Long ticketsSold;
    private BigDecimal totalRevenue;
    private Long totalPurchases;

    // Calculated fields
    private Integer ticketsRemaining;
    private BigDecimal occupancyRate; // Percentage of tickets sold
}
