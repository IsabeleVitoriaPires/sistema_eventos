package com.ticketsystem.application.purchase;

import com.ticketsystem.domain.ticket.Ticket;
import com.ticketsystem.domain.ticket.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for Ticket response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponseDTO {
    private UUID id;
    private UUID purchaseId;
    private UUID eventId;
    private AttendeeDTO attendee;
    private BigDecimal price;
    private TicketStatus status;
    private String ticketCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Convert domain entity to DTO
     */
    public static TicketResponseDTO fromDomain(Ticket ticket) {
        if (ticket == null) return null;
        return TicketResponseDTO.builder()
                .id(ticket.getId())
                .purchaseId(ticket.getPurchaseId())
                .eventId(ticket.getEventId())
                .attendee(AttendeeDTO.fromDomain(ticket.getAttendee()))
                .price(ticket.getPrice())
                .status(ticket.getStatus())
                .ticketCode(ticket.getTicketCode())
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .build();
    }
}
