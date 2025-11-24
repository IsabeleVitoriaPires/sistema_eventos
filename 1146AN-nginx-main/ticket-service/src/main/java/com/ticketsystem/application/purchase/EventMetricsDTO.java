package com.ticketsystem.application.purchase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO for Event Sales Metrics
 * Requisito 6: Dashboard do organizador
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

    public static EventMetricsDTO empty(UUID eventId) {
        return EventMetricsDTO.builder()
                .eventId(eventId)
                .totalTicketsSold(0L)
                .totalRevenue(BigDecimal.ZERO)
                .totalPurchases(0L)
                .build();
    }
}
