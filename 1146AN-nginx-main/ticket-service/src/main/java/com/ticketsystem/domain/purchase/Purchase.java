package com.ticketsystem.domain.purchase;

import com.ticketsystem.domain.ticket.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Purchase Domain Entity
 * Represents a purchase of multiple tickets
 * Requisito 4: Purchase can contain multiple tickets with individual attendee info
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Purchase {
    private UUID id;
    private UUID userId;
    private UUID eventId;
    private List<Ticket> tickets;
    private Integer quantity;
    private BigDecimal totalAmount;
    private PurchaseStatus status;
    private String purchaseCode;  // Unique purchase code
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Business rule: Confirm purchase
     */
    public void confirm() {
        if (status == PurchaseStatus.CONFIRMED) {
            throw new IllegalStateException("Purchase is already confirmed");
        }
        if (status == PurchaseStatus.CANCELLED) {
            throw new IllegalStateException("Cannot confirm a cancelled purchase");
        }
        this.status = PurchaseStatus.CONFIRMED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Business rule: Cancel purchase
     */
    public void cancel() {
        if (status == PurchaseStatus.CANCELLED) {
            throw new IllegalStateException("Purchase is already cancelled");
        }
        this.status = PurchaseStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();

        // Cancel all tickets
        if (tickets != null) {
            tickets.forEach(Ticket::cancel);
        }
    }

    /**
     * Business rule: Calculate total amount
     * Requisito 4.2: Valor total = valor unitário × quantidade
     */
    public void calculateTotalAmount(BigDecimal unitPrice) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Unit price must be zero or positive");
        }
        this.totalAmount = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    /**
     * Business rule: Add ticket to purchase
     */
    public void addTicket(Ticket ticket) {
        if (this.tickets == null) {
            this.tickets = new ArrayList<>();
        }
        ticket.setPurchaseId(this.id);
        this.tickets.add(ticket);
    }

    /**
     * Business rule: Validate purchase data
     */
    public void validate() {
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (eventId == null) {
            throw new IllegalArgumentException("Event ID is required");
        }
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        if (totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total amount must be zero or positive");
        }
        if (purchaseCode == null || purchaseCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Purchase code is required");
        }
        if (tickets == null || tickets.isEmpty()) {
            throw new IllegalArgumentException("At least one ticket is required");
        }
        if (tickets.size() != quantity) {
            throw new IllegalArgumentException("Number of tickets must match quantity");
        }

        // Validate all tickets
        tickets.forEach(Ticket::validate);
    }

    /**
     * Generate unique purchase code
     */
    public static String generatePurchaseCode() {
        return "PUR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
