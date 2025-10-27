package com.ticketsystem.tickets.domain.port.out;

import com.ticketsystem.tickets.domain.model.Purchase;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Porta de saída para persistência de compras
 */
public interface PurchaseRepository {

    Purchase save(Purchase purchase);

    Optional<Purchase> findById(Long id);

    Optional<Purchase> findByPurchaseCode(String purchaseCode);

    List<Purchase> findByUserId(Long userId);

    List<Purchase> findByEventId(Long eventId);

    long countByEventId(Long eventId);

    BigDecimal sumTotalAmountByEventId(Long eventId);

    void deleteById(Long id);
}
