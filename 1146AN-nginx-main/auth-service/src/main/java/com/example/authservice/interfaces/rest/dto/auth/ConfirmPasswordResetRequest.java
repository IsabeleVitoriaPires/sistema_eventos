package com.example.authservice.interfaces.rest.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for confirming password reset with verification code and new password
 * Requisito 3.3: Recuperacao de senha com codigo de verificacao
 */
public record ConfirmPasswordResetRequest(
        @NotBlank(message = "Verification code is required")
        @Size(min = 6, max = 6, message = "Verification code must be exactly 6 digits")
        String code,

        @NotBlank(message = "New password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        String newPassword
) {
}
