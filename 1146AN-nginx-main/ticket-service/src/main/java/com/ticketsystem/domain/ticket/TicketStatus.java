package com.ticketsystem.domain.ticket;

/**
 * Ticket Status Enum
 */
public enum TicketStatus {
    ACTIVE,     // Ticket is valid and can be used
    USED,       // Ticket has been used at the event
    CANCELLED   // Ticket has been cancelled/refunded
}
