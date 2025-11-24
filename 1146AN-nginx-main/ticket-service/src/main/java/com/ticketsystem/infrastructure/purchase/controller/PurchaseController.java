package com.ticketsystem.infrastructure.purchase.controller;

import com.ticketsystem.application.purchase.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for Purchase operations
 * Requisito 4: Compra de ingressos
 *
 * All endpoints require USER role (enforced by gateway)
 */
@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final CreatePurchaseHandler createPurchaseHandler;
    private final GetPurchaseByIdHandler getPurchaseByIdHandler;
    private final GetPurchaseByCodeHandler getPurchaseByCodeHandler;
    private final ListUserPurchasesHandler listUserPurchasesHandler;
    private final CancelPurchaseHandler cancelPurchaseHandler;

    /**
     * Requisito 4: Create a new purchase with multiple tickets
     * POST /api/purchases
     *
     * Request body example:
     * {
     *   "userId": "uuid",
     *   "eventId": "uuid",
     *   "attendees": [
     *     {
     *       "fullName": "João Silva",
     *       "cpf": "12345678901",
     *       "email": "joao@example.com",
     *       "birthDate": "1990-05-15"
     *     }
     *   ]
     * }
     */
    @PostMapping
    public ResponseEntity<PurchaseResponseDTO> createPurchase(
            @Valid @RequestBody CreatePurchaseRequestDTO request) {
        PurchaseResponseDTO purchase = createPurchaseHandler.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(purchase);
    }

    /**
     * Get purchase by ID
     * GET /api/purchases/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<PurchaseResponseDTO> getPurchaseById(@PathVariable UUID id) {
        PurchaseResponseDTO purchase = getPurchaseByIdHandler.execute(id);
        return ResponseEntity.ok(purchase);
    }

    /**
     * Get purchase by purchase code
     * GET /api/purchases/code/{code}
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<PurchaseResponseDTO> getPurchaseByCode(@PathVariable String code) {
        PurchaseResponseDTO purchase = getPurchaseByCodeHandler.execute(code);
        return ResponseEntity.ok(purchase);
    }

    /**
     * List all purchases for a user
     * GET /api/purchases/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PurchaseResponseDTO>> listUserPurchases(@PathVariable UUID userId) {
        List<PurchaseResponseDTO> purchases = listUserPurchasesHandler.execute(userId);
        return ResponseEntity.ok(purchases);
    }

    /**
     * Cancel a purchase
     * DELETE /api/purchases/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelPurchase(@PathVariable UUID id) {
        cancelPurchaseHandler.execute(id);
        return ResponseEntity.noContent().build();
    }
}
