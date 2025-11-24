package com.ticketsystem.application.purchase;

import com.ticketsystem.domain.purchase.Purchase;
import com.ticketsystem.domain.purchase.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Use Case: Get purchase by ID
 */
@Service
@RequiredArgsConstructor
public class GetPurchaseByIdHandler {

    private final PurchaseRepository purchaseRepository;

    public PurchaseResponseDTO execute(UUID purchaseId) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new PurchaseNotFoundException("Purchase not found with id: " + purchaseId));

        return PurchaseResponseDTO.fromDomain(purchase);
    }
}
