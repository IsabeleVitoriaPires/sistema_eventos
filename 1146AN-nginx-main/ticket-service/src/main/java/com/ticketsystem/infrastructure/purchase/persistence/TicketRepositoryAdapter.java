package com.ticketsystem.infrastructure.purchase.persistence;

import com.ticketsystem.domain.ticket.Ticket;
import com.ticketsystem.domain.ticket.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter Pattern: Adapts JPA repository to domain repository interface
 */
@Component
@RequiredArgsConstructor
public class TicketRepositoryAdapter implements TicketRepository {

    private final JpaTicketRepository jpaTicketRepository;

    @Override
    public Ticket save(Ticket ticket) {
        TicketEntity entity = TicketEntity.fromDomain(ticket);
        TicketEntity savedEntity = jpaTicketRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public List<Ticket> saveAll(List<Ticket> tickets) {
        List<TicketEntity> entities = tickets.stream()
                .map(TicketEntity::fromDomain)
                .collect(Collectors.toList());

        List<TicketEntity> savedEntities = jpaTicketRepository.saveAll(entities);

        return savedEntities.stream()
                .map(TicketEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Ticket> findById(UUID id) {
        return jpaTicketRepository.findById(id)
                .map(TicketEntity::toDomain);
    }

    @Override
    public List<Ticket> findByPurchaseId(UUID purchaseId) {
        return jpaTicketRepository.findByPurchaseId(purchaseId)
                .stream()
                .map(TicketEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Ticket> findByTicketCode(String ticketCode) {
        return jpaTicketRepository.findByTicketCode(ticketCode)
                .map(TicketEntity::toDomain);
    }

    @Override
    public List<Ticket> findByEventId(UUID eventId) {
        return jpaTicketRepository.findByEventId(eventId)
                .stream()
                .map(TicketEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Long countByEventId(UUID eventId) {
        return jpaTicketRepository.countByEventId(eventId);
    }
}
