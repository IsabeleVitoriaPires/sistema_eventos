package com.ticketsystem.infrastructure.purchase.persistence;

import com.ticketsystem.domain.purchase.Purchase;
import com.ticketsystem.domain.purchase.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter Pattern: Adapts JPA repository to domain repository interface
 */
@Component
@RequiredArgsConstructor
public class PurchaseRepositoryAdapter implements PurchaseRepository {

    private final JpaPurchaseRepository jpaPurchaseRepository;

    @Override
    public Purchase save(Purchase purchase) {
        PurchaseEntity entity = PurchaseEntity.fromDomain(purchase);
        PurchaseEntity savedEntity = jpaPurchaseRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Purchase> findById(UUID id) {
        return jpaPurchaseRepository.findById(id)
                .map(PurchaseEntity::toDomain);
    }

    @Override
    public List<Purchase> findByUserId(UUID userId) {
        return jpaPurchaseRepository.findByUserId(userId)
                .stream()
                .map(PurchaseEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Purchase> findByEventId(UUID eventId) {
        return jpaPurchaseRepository.findByEventId(eventId)
                .stream()
                .map(PurchaseEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Purchase> findByPurchaseCode(String purchaseCode) {
        return jpaPurchaseRepository.findByPurchaseCode(purchaseCode)
                .map(PurchaseEntity::toDomain);
    }

    @Override
    public Long countTicketsByEventId(UUID eventId) {
        Long count = jpaPurchaseRepository.countTicketsByEventId(eventId);
        return count != null ? count : 0L;
    }

    @Override
    public BigDecimal sumTotalAmountByEventId(UUID eventId) {
        BigDecimal sum = jpaPurchaseRepository.sumTotalAmountByEventId(eventId);
        return sum != null ? sum : BigDecimal.ZERO;
    }
}
