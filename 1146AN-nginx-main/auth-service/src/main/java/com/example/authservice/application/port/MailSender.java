package com.example.authservice.application.port;

import java.time.Instant;

public interface MailSender {
    void sendMagicLink(
        String toEmail,
        String magicUrl,
        Instant expiresAt
    );
}
