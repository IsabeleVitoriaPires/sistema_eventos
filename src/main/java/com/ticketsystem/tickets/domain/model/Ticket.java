package com.ticketsystem.tickets.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long eventId;

    @Column(nullable = false)
    private Long purchaseId;

    @Column(nullable = false)
    private String attendeeName;

    @Column(nullable = false)
    private String attendeeCpf;

    @Column(nullable = false)
    private String attendeeEmail;

    @Column(nullable = false)
    private LocalDate attendeeBirthDate;

    @Column(nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status;

    @Column(nullable = false, unique = true)
    private String ticketCode;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = TicketStatus.ACTIVE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (status == TicketStatus.CANCELLED) {
            throw new IllegalStateException("Ingresso já está cancelado");
        }
        if (status == TicketStatus.USED) {
            throw new IllegalStateException("Não é possível cancelar um ingresso já utilizado");
        }
        this.status = TicketStatus.CANCELLED;
    }

    public void markAsUsed() {
        if (status == TicketStatus.CANCELLED) {
            throw new IllegalStateException("Não é possível usar um ingresso cancelado");
        }
        if (status == TicketStatus.USED) {
            throw new IllegalStateException("Ingresso já foi utilizado");
        }
        this.status = TicketStatus.USED;
    }

    public enum TicketStatus {
        ACTIVE,
        USED,
        CANCELLED
    }
}
