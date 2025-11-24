package com.ticketsystem.application.purchase;

import com.ticketsystem.domain.purchase.Purchase;
import com.ticketsystem.domain.purchase.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Use Case: Get purchase by purchase code
 */
@Service
@RequiredArgsConstructor
public class GetPurchaseByCodeHandler {

    private final PurchaseRepository purchaseRepository;

    public PurchaseResponseDTO execute(String purchaseCode) {
        Purchase purchase = purchaseRepository.findByPurchaseCode(purchaseCode)
                .orElseThrow(() -> new PurchaseNotFoundException("Purchase not found with code: " + purchaseCode));

        return PurchaseResponseDTO.fromDomain(purchase);
    }
}
