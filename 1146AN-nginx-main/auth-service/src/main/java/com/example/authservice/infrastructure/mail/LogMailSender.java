package com.example.authservice.infrastructure.mail;

import com.example.authservice.application.port.MailSender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Development Mail Sender - Logs emails instead of sending
 * Used in dev, local, and test profiles
 */
@Component
@Profile({"dev", "local", "test"})
public class LogMailSender implements MailSender {
    private static final Logger log = LoggerFactory.getLogger(LogMailSender.class);

    @Override
    public void sendMagicLink(String toEmail, String magicUrl, Instant expiresAt) {
        log.info("[DEV] Magic Link para {}: {} (expira em {})", toEmail, magicUrl, expiresAt);
    }

    @Override
    public void sendPasswordResetCode(String toEmail, String verificationCode) {
        log.info("=================================================");
        log.info("[DEV] Password Reset Code para: {}", toEmail);
        log.info("[DEV] Codigo de Verificacao: {}", verificationCode);
        log.info("[DEV] Este codigo expira em 15 minutos");
        log.info("=================================================");
    }
}
