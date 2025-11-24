package com.ticketsystem.infrastructure.purchase.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA Repository for PurchaseEntity
 */
@Repository
public interface JpaPurchaseRepository extends JpaRepository<PurchaseEntity, UUID> {

    List<PurchaseEntity> findByUserId(UUID userId);

    List<PurchaseEntity> findByEventId(UUID eventId);

    Optional<PurchaseEntity> findByPurchaseCode(String purchaseCode);

    @Query("SELECT SUM(p.quantity) FROM PurchaseEntity p WHERE p.eventId = :eventId AND p.status = 'CONFIRMED'")
    Long countTicketsByEventId(@Param("eventId") UUID eventId);

    @Query("SELECT SUM(p.totalAmount) FROM PurchaseEntity p WHERE p.eventId = :eventId AND p.status = 'CONFIRMED'")
    BigDecimal sumTotalAmountByEventId(@Param("eventId") UUID eventId);
}
