package com.eventservice.application.event;

import com.eventservice.domain.event.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for Event responses
 * Used to transfer event data to the client
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponseDTO {
    private UUID id;
    private String name;
    private String description;
    private LocalDateTime eventDate;
    private String location;
    private BigDecimal ticketPrice;
    private Integer totalTickets;
    private Integer availableTickets;
    private UUID organizerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Factory method: Convert domain entity to DTO
     */
    public static EventResponseDTO fromDomain(Event event) {
        return EventResponseDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .location(event.getLocation())
                .ticketPrice(event.getTicketPrice())
                .totalTickets(event.getTotalTickets())
                .availableTickets(event.getAvailableTickets())
                .organizerId(event.getOrganizerId())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .build();
    }
}
