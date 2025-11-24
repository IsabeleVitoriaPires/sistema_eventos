package com.example.authservice.interfaces.rest.dto.auth;

import com.example.authservice.interfaces.rest.dto.user.UserResponse;

public record TokenResponse (
    String accessToken,
    String refreshToken,
    long expiresIn,
    UserResponse user
) {}
