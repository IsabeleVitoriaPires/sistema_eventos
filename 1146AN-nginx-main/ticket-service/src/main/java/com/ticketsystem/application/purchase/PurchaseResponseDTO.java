package com.ticketsystem.application.purchase;

import com.ticketsystem.domain.purchase.Purchase;
import com.ticketsystem.domain.purchase.PurchaseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * DTO for Purchase response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseResponseDTO {
    private UUID id;
    private UUID userId;
    private UUID eventId;
    private List<TicketResponseDTO> tickets;
    private Integer quantity;
    private BigDecimal totalAmount;
    private PurchaseStatus status;
    private String purchaseCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Convert domain entity to DTO
     */
    public static PurchaseResponseDTO fromDomain(Purchase purchase) {
        if (purchase == null) return null;

        List<TicketResponseDTO> ticketDTOs = null;
        if (purchase.getTickets() != null) {
            ticketDTOs = purchase.getTickets().stream()
                    .map(TicketResponseDTO::fromDomain)
                    .collect(Collectors.toList());
        }

        return PurchaseResponseDTO.builder()
                .id(purchase.getId())
                .userId(purchase.getUserId())
                .eventId(purchase.getEventId())
                .tickets(ticketDTOs)
                .quantity(purchase.getQuantity())
                .totalAmount(purchase.getTotalAmount())
                .status(purchase.getStatus())
                .purchaseCode(purchase.getPurchaseCode())
                .createdAt(purchase.getCreatedAt())
                .updatedAt(purchase.getUpdatedAt())
                .build();
    }
}
