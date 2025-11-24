package com.ticketsystem.application.purchase;

import com.ticketsystem.domain.ticket.Attendee;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for Attendee information
 * Requisito 4.3: Each ticket requires individual attendee data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendeeDTO {

    @NotBlank(message = "Full name is required")
    @Size(min = 3, max = 200, message = "Full name must be between 3 and 200 characters")
    private String fullName;

    @NotBlank(message = "CPF is required")
    @Pattern(regexp = "\\d{11}|\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF must be in format: 11111111111 or 111.111.111-11")
    private String cpf;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    /**
     * Convert DTO to domain model
     */
    public Attendee toDomain() {
        return Attendee.builder()
                .fullName(this.fullName)
                .cpf(this.cpf)
                .email(this.email)
                .birthDate(this.birthDate)
                .build();
    }

    /**
     * Convert domain model to DTO
     */
    public static AttendeeDTO fromDomain(Attendee attendee) {
        if (attendee == null) return null;
        return AttendeeDTO.builder()
                .fullName(attendee.getFullName())
                .cpf(attendee.getCpf())
                .email(attendee.getEmail())
                .birthDate(attendee.getBirthDate())
                .build();
    }
}
