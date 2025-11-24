package com.ticketsystem.infrastructure.purchase.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA Repository for TicketEntity
 */
@Repository
public interface JpaTicketRepository extends JpaRepository<TicketEntity, UUID> {

    List<TicketEntity> findByPurchaseId(UUID purchaseId);

    Optional<TicketEntity> findByTicketCode(String ticketCode);

    List<TicketEntity> findByEventId(UUID eventId);

    Long countByEventId(UUID eventId);
}
