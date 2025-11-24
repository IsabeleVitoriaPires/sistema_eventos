package com.eventservice.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Event Domain Entity
 * Represents an event in the system following DDD principles
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
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
     * Business rule: Check if there are available tickets
     */
    public boolean hasAvailableTickets() {
        return availableTickets != null && availableTickets > 0;
    }

    /**
     * Business rule: Check if a specific quantity is available
     */
    public boolean hasAvailableTickets(int quantity) {
        return availableTickets != null && availableTickets >= quantity;
    }

    /**
     * Business rule: Reserve tickets (decreases available tickets)
     * Throws exception if not enough tickets available
     */
    public void reserveTickets(int quantity) {
        if (!hasAvailableTickets(quantity)) {
            throw new InsufficientTicketsException(
                "Not enough tickets available. Requested: " + quantity + ", Available: " + availableTickets
            );
        }
        this.availableTickets -= quantity;
    }

    /**
     * Business rule: Release tickets back (increases available tickets)
     * Used when a purchase is cancelled
     */
    public void releaseTickets(int quantity) {
        if (this.availableTickets + quantity > this.totalTickets) {
            throw new IllegalStateException("Cannot release more tickets than total tickets");
        }
        this.availableTickets += quantity;
    }

    /**
     * Business rule: Check if event is in the past
     */
    public boolean isPastEvent() {
        return eventDate != null && eventDate.isBefore(LocalDateTime.now());
    }

    /**
     * Business rule: Validate event data
     */
    public void validate() {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Event name is required");
        }
        if (eventDate == null) {
            throw new IllegalArgumentException("Event date is required");
        }
        if (eventDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Event date must be in the future");
        }
        if (ticketPrice == null || ticketPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Ticket price must be zero or positive");
        }
        if (totalTickets == null || totalTickets <= 0) {
            throw new IllegalArgumentException("Total tickets must be greater than zero");
        }
        if (availableTickets == null || availableTickets < 0 || availableTickets > totalTickets) {
            throw new IllegalArgumentException("Available tickets must be between 0 and total tickets");
        }
        if (organizerId == null) {
            throw new IllegalArgumentException("Organizer ID is required");
        }
    }
}
