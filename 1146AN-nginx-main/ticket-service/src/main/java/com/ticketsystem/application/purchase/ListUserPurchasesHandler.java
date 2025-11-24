package com.ticketsystem.application.purchase;

import com.ticketsystem.domain.purchase.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Use Case: List all purchases for a specific user
 * Requisito 4: User can view their purchase history
 */
@Service
@RequiredArgsConstructor
public class ListUserPurchasesHandler {

    private final PurchaseRepository purchaseRepository;

    public List<PurchaseResponseDTO> execute(UUID userId) {
        return purchaseRepository.findByUserId(userId)
                .stream()
                .map(PurchaseResponseDTO::fromDomain)
                .collect(Collectors.toList());
    }
}
