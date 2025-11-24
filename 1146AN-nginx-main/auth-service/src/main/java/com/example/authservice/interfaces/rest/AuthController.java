package com.example.authservice.interfaces.rest;

import com.example.authservice.application.auth.*;
import com.example.authservice.interfaces.rest.dto.auth.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication Controller
 * Handles login and password recovery
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final PasswordLoginHandler passwordLoginHandler;
    private final RequestMagicLinkHandler requestMagicLinkHandler;
    private final VerifyMagicLinkHandler verifyMagicLinkHandler;
    private final RequestPasswordResetHandler requestPasswordResetHandler;
    private final ConfirmPasswordResetHandler confirmPasswordResetHandler;

    /**
     * Requisito 3.1: Login with email and password
     * POST /auth/login/password
     */
    @PostMapping("/login/password")
    public ResponseEntity<TokenResponse> loginWithPassword(@Valid @RequestBody PasswordLoginRequest request) {
        TokenResponse response = passwordLoginHandler.handle(request.email(), request.password());
        return ResponseEntity.ok(response);
    }

    /**
     * Alternative login: Request magic link
     * POST /auth/login/magic
     */
    @PostMapping("/login/magic")
    public ResponseEntity<Void> requestMagic(@Valid @RequestBody MagicLinkRequest req) {
        requestMagicLinkHandler.handle(req.email());
        return ResponseEntity.accepted().build();
    }

    /**
     * Alternative login: Verify magic link token
     * POST /auth/login/magic/verify
     */
    @PostMapping("/login/magic/verify")
    public ResponseEntity<TokenResponse> verifyMagic(@Valid @RequestBody MagicLinkVerifyRequest req) {
        TokenResponse tokens = verifyMagicLinkHandler.handle(req.token());
        return ResponseEntity.ok(tokens);
    }

    /**
     * Requisito 3.3: Request password reset
     * POST /auth/password/reset/request
     * Body: { "email": "user@example.com" }
     */
    @PostMapping("/password/reset/request")
    public ResponseEntity<Void> requestPasswordReset(@Valid @RequestBody RequestPasswordResetRequest req) {
        requestPasswordResetHandler.handle(req.email());
        return ResponseEntity.accepted().build();
    }

    /**
     * Requisito 3.3: Confirm password reset with verification code
     * POST /auth/password/reset/confirm
     * Body: { "code": "123456", "newPassword": "..." }
     */
    @PostMapping("/password/reset/confirm")
    public ResponseEntity<Void> confirmPasswordReset(@Valid @RequestBody ConfirmPasswordResetRequest req) {
        confirmPasswordResetHandler.handle(req.code(), req.newPassword());
        return ResponseEntity.ok().build();
    }
}
