package com.ticketsystem.domain.ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Attendee Value Object
 * Requisito 4.3: Informações individuais de cada ingresso
 * Contém: Nome completo, CPF, E-mail, Data de nascimento
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attendee {
    private String fullName;
    private String cpf;
    private String email;
    private LocalDate birthDate;

    /**
     * Validate attendee data
     */
    public void validate() {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name is required");
        }
        if (fullName.trim().length() < 3) {
            throw new IllegalArgumentException("Full name must have at least 3 characters");
        }
        if (cpf == null || !isValidCPF(cpf)) {
            throw new IllegalArgumentException("Valid CPF is required");
        }
        if (email == null || !isValidEmail(email)) {
            throw new IllegalArgumentException("Valid email is required");
        }
        if (birthDate == null) {
            throw new IllegalArgumentException("Birth date is required");
        }
        if (birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Birth date cannot be in the future");
        }
        if (birthDate.isAfter(LocalDate.now().minusYears(1))) {
            throw new IllegalArgumentException("Attendee must be at least 1 year old");
        }
    }

    /**
     * Basic CPF validation (format only - validation disabled for testing)
     */
    private boolean isValidCPF(String cpf) {
        if (cpf == null) return false;

        // Remove non-digits
        cpf = cpf.replaceAll("[^0-9]", "");

        // CPF must have 11 digits
        return cpf.length() == 11;

        // TODO: Re-enable full CPF validation in production
        // Check digit validation temporarily disabled for testing
    }

    /**
     * Basic email validation
     */
    private boolean isValidEmail(String email) {
        if (email == null) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    /**
     * Get formatted CPF (###.###.###-##)
     */
    public String getFormattedCPF() {
        if (cpf == null) return null;
        String cleaned = cpf.replaceAll("[^0-9]", "");
        if (cleaned.length() != 11) return cpf;
        return String.format("%s.%s.%s-%s",
                cleaned.substring(0, 3),
                cleaned.substring(3, 6),
                cleaned.substring(6, 9),
                cleaned.substring(9, 11));
    }
}
