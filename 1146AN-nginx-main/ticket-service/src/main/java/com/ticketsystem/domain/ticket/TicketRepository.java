package com.ticketsystem.domain.ticket;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Ticket Repository Port (Interface)
 * Defines the contract for ticket persistence
 * Implementation will be in the infrastructure layer
 */
public interface TicketRepository {
    Ticket save(Ticket ticket);
    List<Ticket> saveAll(List<Ticket> tickets);
    Optional<Ticket> findById(UUID id);
    List<Ticket> findByPurchaseId(UUID purchaseId);
    Optional<Ticket> findByTicketCode(String ticketCode);
    List<Ticket> findByEventId(UUID eventId);
    Long countByEventId(UUID eventId);
}
