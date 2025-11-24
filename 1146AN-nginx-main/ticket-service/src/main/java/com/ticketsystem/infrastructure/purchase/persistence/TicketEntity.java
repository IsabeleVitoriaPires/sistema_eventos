package com.ticketsystem.infrastructure.purchase.persistence;

import com.ticketsystem.domain.ticket.Ticket;
import com.ticketsystem.domain.ticket.TicketStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Entity for Ticket
 */
@Entity
@Table(name = "tickets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "purchase_id", nullable = false)
    private UUID purchaseId;

    @Column(name = "event_id", nullable = false)
    private UUID eventId;

    @Embedded
    private AttendeeEntity attendee;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private TicketStatus status;

    @Column(name = "ticket_code", nullable = false, unique = true, length = 50)
    private String ticketCode;

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

    public Ticket toDomain() {
        return Ticket.builder()
                .id(this.id)
                .purchaseId(this.purchaseId)
                .eventId(this.eventId)
                .attendee(this.attendee != null ? this.attendee.toDomain() : null)
                .price(this.price)
                .status(this.status)
                .ticketCode(this.ticketCode)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }

    public static TicketEntity fromDomain(Ticket ticket) {
        return TicketEntity.builder()
                .id(ticket.getId())
                .purchaseId(ticket.getPurchaseId())
                .eventId(ticket.getEventId())
                .attendee(AttendeeEntity.fromDomain(ticket.getAttendee()))
                .price(ticket.getPrice())
                .status(ticket.getStatus())
                .ticketCode(ticket.getTicketCode())
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .build();
    }
}
