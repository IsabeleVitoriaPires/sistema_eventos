package com.example.authservice.application.port;

import java.time.Instant;

/**
 * Port (Interface) for sending emails
 * Implementation will be in infrastructure layer
 */
public interface MailSender {
    /**
     * Send magic link email for passwordless login
     */
    void sendMagicLink(
        String toEmail,
        String magicUrl,
        Instant expiresAt
    );

    /**
     * Send password reset verification code email
     * Requisito 3.3: Enviar e-mail com codigo de verificacao para redefinicao de senha
     */
    void sendPasswordResetCode(
        String toEmail,
        String verificationCode
    );
}
