package com.ticketsystem.application.purchase;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * DTO for creating a new purchase
 * Requisito 4: Purchase with multiple tickets and individual attendee information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePurchaseRequestDTO {

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotNull(message = "Event ID is required")
    private UUID eventId;

    @NotEmpty(message = "At least one attendee is required")
    @Size(min = 1, max = 50, message = "Purchase must have between 1 and 50 tickets")
    @Valid
    private List<AttendeeDTO> attendees;

    /**
     * Get quantity of tickets (number of attendees)
     * Requisito 4.1: User can select quantity
     */
    public Integer getQuantity() {
        return attendees != null ? attendees.size() : 0;
    }
}
