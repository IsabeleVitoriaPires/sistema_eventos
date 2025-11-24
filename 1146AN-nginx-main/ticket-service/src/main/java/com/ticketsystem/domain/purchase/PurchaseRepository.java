package com.ticketsystem.domain.purchase;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Purchase Repository Port (Interface)
 * Defines the contract for purchase persistence
 * Implementation will be in the infrastructure layer
 */
public interface PurchaseRepository {
    Purchase save(Purchase purchase);
    Optional<Purchase> findById(UUID id);
    List<Purchase> findByUserId(UUID userId);
    List<Purchase> findByEventId(UUID eventId);
    Optional<Purchase> findByPurchaseCode(String purchaseCode);

    // Dashboard queries (Requisito 6)
    Long countTicketsByEventId(UUID eventId);
    BigDecimal sumTotalAmountByEventId(UUID eventId);
}
