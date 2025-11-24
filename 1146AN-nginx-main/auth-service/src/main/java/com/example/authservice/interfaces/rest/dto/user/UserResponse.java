package com.example.authservice.interfaces.rest.dto.user;

import java.util.UUID;

/**
 * DTO for User response
 * Returns user data after registration or queries
 */
public record UserResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String role
) {
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
