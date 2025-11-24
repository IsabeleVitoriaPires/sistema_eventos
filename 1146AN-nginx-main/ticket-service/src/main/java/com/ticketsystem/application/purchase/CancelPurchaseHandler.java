package com.ticketsystem.application.purchase;

import com.ticketsystem.domain.purchase.Purchase;
import com.ticketsystem.domain.purchase.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use Case: Cancel a purchase and its tickets
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CancelPurchaseHandler {

    private final PurchaseRepository purchaseRepository;

    @Transactional
    public void execute(UUID purchaseId) {
        log.info("Cancelling purchase: {}", purchaseId);

        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new PurchaseNotFoundException("Purchase not found with id: " + purchaseId));

        // Cancel purchase (also cancels all tickets)
        purchase.cancel();

        // Save changes
        purchaseRepository.save(purchase);

        log.info("Purchase cancelled successfully: {}", purchaseId);

        /*
         * TODO: In a real system, we should also release the tickets back
         * to the event-service, increasing available tickets count.
         * This would require implementing a release endpoint in event-service.
         */
    }
}
