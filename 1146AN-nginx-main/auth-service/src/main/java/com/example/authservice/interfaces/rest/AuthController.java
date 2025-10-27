package com.example.authservice.interfaces.rest;

import com.example.authservice.application.auth.PasswordLoginHandler;
import com.example.authservice.application.auth.RequestMagicLinkHandler;
import com.example.authservice.application.auth.VerifyMagicLinkHandler;
import com.example.authservice.interfaces.rest.dto.auth.MagicLinkRequest;
import com.example.authservice.interfaces.rest.dto.auth.MagicLinkVerifyRequest;
import com.example.authservice.interfaces.rest.dto.auth.PasswordLoginRequest;
import com.example.authservice.interfaces.rest.dto.auth.TokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final PasswordLoginHandler passwordLoginHandler;
    private final RequestMagicLinkHandler requestMagicLinkHandler;
    private final VerifyMagicLinkHandler verifyMagicLinkHandler;

    @PostMapping("/login/password")
    public ResponseEntity<TokenResponse> loginWithPassword(@Valid @RequestBody PasswordLoginRequest request) {
        TokenResponse response = passwordLoginHandler.handle(request.email(), request.password());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login/magic")
    public ResponseEntity<Void> requestMagic(@Valid @RequestBody MagicLinkRequest req) {
        requestMagicLinkHandler.handle(req.email());
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/login/magic/verify")
    public ResponseEntity<TokenResponse> verifyMagic(@Valid @RequestBody MagicLinkVerifyRequest req) {
        TokenResponse tokens = verifyMagicLinkHandler.handle(req.token());
        return ResponseEntity.ok(tokens);
    }
}
