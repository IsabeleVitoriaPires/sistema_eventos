package com.ticketsystem.tickets.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchases")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long eventId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PurchaseStatus status;

    @Column(nullable = false, unique = true)
    private String purchaseCode;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = PurchaseStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void confirm() {
        if (status != PurchaseStatus.PENDING) {
            throw new IllegalStateException("Apenas compras pendentes podem ser confirmadas");
        }
        this.status = PurchaseStatus.CONFIRMED;
    }

    public void cancel() {
        if (status == PurchaseStatus.CANCELLED) {
            throw new IllegalStateException("Compra já está cancelada");
        }
        this.status = PurchaseStatus.CANCELLED;
    }

    public enum PurchaseStatus {
        PENDING,
        CONFIRMED,
        CANCELLED
    }
}
