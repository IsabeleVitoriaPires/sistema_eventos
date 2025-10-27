package com.ticketsystem.tickets.domain.port.out;

import com.ticketsystem.tickets.domain.model.Ticket;

import java.util.List;
import java.util.Optional;

/**
 * Porta de saída para persistência de ingressos
 */
public interface TicketRepository {

    Ticket save(Ticket ticket);

    Optional<Ticket> findById(Long id);

    Optional<Ticket> findByTicketCode(String ticketCode);

    List<Ticket> findByPurchaseId(Long purchaseId);

    List<Ticket> findByEventId(Long eventId);

    long countByEventId(Long eventId);

    void deleteById(Long id);
}
