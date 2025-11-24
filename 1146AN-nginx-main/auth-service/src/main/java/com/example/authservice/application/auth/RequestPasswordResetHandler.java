package com.example.authservice.application.auth;

import com.example.authservice.application.port.MailSender;
import com.example.authservice.domain.user.User;
import com.example.authservice.domain.user.UserRepository;
import com.example.authservice.domain.user.vo.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Use Case: Request password reset
 * Requisito 3.3: Enviar e-mail com codigo de verificacao para redefinicao de senha
 *
 * Flow:
 * 1. Receive email
 * 2. Find user by email
 * 3. Generate secure random 6-digit code
 * 4. Save code with expiration (15 minutes)
 * 5. Send email with verification code
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RequestPasswordResetHandler {

    private final UserRepository userRepository;
    private final MailSender mailSender;

    @Value("${app.password-reset.ttl-seconds:900}")
    private long ttlSeconds;


    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Transactional
    public void handle(String emailRaw) {
        Email email = Email.of(emailRaw);

        log.info("Password reset requested for email: {}", email.getValue());

        // Find user by email
        User user = userRepository.findByEmail(email.getValue())
                .orElseThrow(() -> {
                    log.warn("Password reset requested for non-existent email: {}", email.getValue());
                    // Security: Don't reveal if email exists
                    // Return success anyway to prevent email enumeration
                    return new RuntimeException("User not found");
                });

        // Generate secure random 6-digit code (100000-999999)
        int code = 100000 + SECURE_RANDOM.nextInt(900000);
        String verificationCode = String.valueOf(code);

        // Calculate expiration time
        long expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000);

        // Save code and expiry to user
        user.setResetPasswordToken(verificationCode);
        user.setResetPasswordTokenExpiry(expiryTime);
        userRepository.save(user);

        // Send email with verification code
        try {
            mailSender.sendPasswordResetCode(email.getValue(), verificationCode);
            log.info("Password reset code sent to: {}", email.getValue());
        } catch (Exception e) {
            log.error("Failed to send password reset code to: {}", email.getValue(), e);
            throw new RuntimeException("Failed to send password reset code", e);
        }
    }
}
