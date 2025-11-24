package com.eventservice.application.event;

import com.eventservice.domain.event.Event;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for creating a new event
 * Contains validation constraints
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventRequestDTO {

    @NotBlank(message = "Event name is required")
    @Size(min = 3, max = 200, message = "Event name must be between 3 and 200 characters")
    private String name;

    @NotBlank(message = "Event description is required")
    @Size(min = 10, max = 2000, message = "Description must be between 10 and 2000 characters")
    private String description;

    @NotNull(message = "Event date is required")
    @Future(message = "Event date must be in the future")
    private LocalDateTime eventDate;

    @NotBlank(message = "Location is required")
    @Size(min = 3, max = 300, message = "Location must be between 3 and 300 characters")
    private String location;

    @NotNull(message = "Ticket price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Ticket price must be zero or positive")
    private BigDecimal ticketPrice;

    @NotNull(message = "Total tickets is required")
    @Min(value = 1, message = "Total tickets must be at least 1")
    private Integer totalTickets;

    @NotNull(message = "Organizer ID is required")
    private UUID organizerId;

    /**
     * Factory method: Convert DTO to domain entity
     */
    public Event toDomain() {
        return Event.builder()
                .id(UUID.randomUUID())
                .name(this.name)
                .description(this.description)
                .eventDate(this.eventDate)
                .location(this.location)
                .ticketPrice(this.ticketPrice)
                .totalTickets(this.totalTickets)
                .availableTickets(this.totalTickets) // Initially, all tickets are available
                .organizerId(this.organizerId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
