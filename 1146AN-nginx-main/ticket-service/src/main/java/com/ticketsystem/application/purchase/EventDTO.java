package com.ticketsystem.application.purchase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for Event data from event-service
 * Used for integration between services
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private UUID id;
    private String name;
    private String description;
    private LocalDateTime eventDate;
    private String location;
    private BigDecimal ticketPrice;
    private Integer totalTickets;
    private Integer availableTickets;
    private UUID organizerId;

    public boolean hasAvailableTickets(int quantity) {
        return availableTickets != null && availableTickets >= quantity;
    }
}
