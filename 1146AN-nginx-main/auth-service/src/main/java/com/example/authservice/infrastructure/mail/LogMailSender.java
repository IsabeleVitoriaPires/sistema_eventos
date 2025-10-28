package com.example.authservice.infrastructure.mail;

import com.example.authservice.application.port.MailSender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Profile({"dev", "local", "test"})
public class LogMailSender implements MailSender {
    private static final Logger log = LoggerFactory.getLogger(LogMailSender.class);

    @Override
    public void sendMagicLink(String toEmail, String magicUrl, Instant expiresAt) {
        log.info("[DEV] Magic Link para {}: {} (expira em {})", toEmail, magicUrl, expiresAt);
    }
}
