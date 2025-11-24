package com.ticketsystem.domain.ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Ticket Domain Entity
 * Represents a single ticket for an event
 * Requisito 4: Each ticket has individual attendee information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    private UUID id;
    private UUID purchaseId;
    private UUID eventId;
    private Attendee attendee;  // Nome, CPF, Email, Data de nascimento
    private BigDecimal price;
    private TicketStatus status;
    private String ticketCode;  // Unique code for ticket validation
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Business rule: Cancel ticket
     */
    public void cancel() {
        if (status == TicketStatus.CANCELLED) {
            throw new IllegalStateException("Ticket is already cancelled");
        }
        if (status == TicketStatus.USED) {
            throw new IllegalStateException("Cannot cancel a used ticket");
        }
        this.status = TicketStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Business rule: Mark ticket as used
     */
    public void markAsUsed() {
        if (status == TicketStatus.CANCELLED) {
            throw new IllegalStateException("Cannot use a cancelled ticket");
        }
        if (status == TicketStatus.USED) {
            throw new IllegalStateException("Ticket is already used");
        }
        this.status = TicketStatus.USED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Business rule: Validate ticket data
     */
    public void validate() {
        if (eventId == null) {
            throw new IllegalArgumentException("Event ID is required");
        }
        if (attendee == null) {
            throw new IllegalArgumentException("Attendee information is required");
        }
        attendee.validate();  // Validate attendee data
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price must be zero or positive");
        }
        if (ticketCode == null || ticketCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Ticket code is required");
        }
    }

    /**
     * Generate unique ticket code
     */
    public static String generateTicketCode() {
        return "TKT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
