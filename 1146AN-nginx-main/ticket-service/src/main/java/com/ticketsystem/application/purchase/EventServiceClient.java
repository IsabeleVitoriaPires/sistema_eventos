package com.ticketsystem.application.purchase;

import java.util.UUID;

/**
 * Port (Interface) for Event Service communication
 * Implementation will be in infrastructure layer
 */
public interface EventServiceClient {
    /**
     * Get event details from event-service
     * @param eventId Event UUID
     * @return Event DTO
     * @throws EventNotFoundException if event not found
     */
    EventDTO getEvent(UUID eventId);

    /**
     * Reserve tickets for an event
     * @param eventId Event UUID
     * @param quantity Number of tickets to reserve
     * @throws InsufficientTicketsException if not enough tickets available
     */
    void reserveTickets(UUID eventId, int quantity);
}
