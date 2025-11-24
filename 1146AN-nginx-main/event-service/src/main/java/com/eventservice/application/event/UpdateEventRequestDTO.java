package com.eventservice.application.event;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for updating an existing event
 * All fields are optional (null means no update)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequestDTO {

    @Size(min = 3, max = 200, message = "Event name must be between 3 and 200 characters")
    private String name;

    @Size(min = 10, max = 2000, message = "Description must be between 10 and 2000 characters")
    private String description;

    @Future(message = "Event date must be in the future")
    private LocalDateTime eventDate;

    @Size(min = 3, max = 300, message = "Location must be between 3 and 300 characters")
    private String location;

    @DecimalMin(value = "0.0", inclusive = true, message = "Ticket price must be zero or positive")
    private BigDecimal ticketPrice;

    @Min(value = 1, message = "Total tickets must be at least 1")
    private Integer totalTickets;
}
