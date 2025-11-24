package com.ticketsystem.infrastructure.purchase.persistence;

import com.ticketsystem.domain.purchase.Purchase;
import com.ticketsystem.domain.purchase.PurchaseStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * JPA Entity for Purchase
 */
@Entity
@Table(name = "purchases")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "event_id", nullable = false)
    private UUID eventId;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "purchase_id")
    private List<TicketEntity> tickets;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PurchaseStatus status;

    @Column(name = "purchase_code", nullable = false, unique = true, length = 50)
    private String purchaseCode;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Purchase toDomain() {
        List<com.ticketsystem.domain.ticket.Ticket> domainTickets = null;
        if (this.tickets != null) {
            domainTickets = this.tickets.stream()
                    .map(TicketEntity::toDomain)
                    .collect(Collectors.toList());
        }

        return Purchase.builder()
                .id(this.id)
                .userId(this.userId)
                .eventId(this.eventId)
                .tickets(domainTickets)
                .quantity(this.quantity)
                .totalAmount(this.totalAmount)
                .status(this.status)
                .purchaseCode(this.purchaseCode)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }

    public static PurchaseEntity fromDomain(Purchase purchase) {
        List<TicketEntity> ticketEntities = null;
        if (purchase.getTickets() != null) {
            ticketEntities = purchase.getTickets().stream()
                    .map(TicketEntity::fromDomain)
                    .collect(Collectors.toList());
        }

        return PurchaseEntity.builder()
                .id(purchase.getId())
                .userId(purchase.getUserId())
                .eventId(purchase.getEventId())
                .tickets(ticketEntities != null ? ticketEntities : new ArrayList<>())
                .quantity(purchase.getQuantity())
                .totalAmount(purchase.getTotalAmount())
                .status(purchase.getStatus())
                .purchaseCode(purchase.getPurchaseCode())
                .createdAt(purchase.getCreatedAt())
                .updatedAt(purchase.getUpdatedAt())
                .build();
    }
}
