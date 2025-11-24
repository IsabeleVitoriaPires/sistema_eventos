package com.ticketsystem.application.purchase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for Organizer Dashboard
 * Requisito 6: Dashboard com metricas agregadas de todos os eventos
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizerDashboardDTO {
    private Long totalEvents;
    private Long totalTicketsSold;
    private BigDecimal totalRevenue;
    private List<EventMetricsDTO> eventMetrics;

    public static OrganizerDashboardDTO empty() {
        return OrganizerDashboardDTO.builder()
                .totalEvents(0L)
                .totalTicketsSold(0L)
                .totalRevenue(BigDecimal.ZERO)
                .eventMetrics(List.of())
                .build();
    }
}
