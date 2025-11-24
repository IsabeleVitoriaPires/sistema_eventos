package com.example.authservice.application.auth;

import com.example.authservice.application.port.PasswordHasher;
import com.example.authservice.domain.user.User;
import com.example.authservice.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * Use Case: Confirm password reset with verification code
 * Requisito 3.3: Redefinicao de senha com codigo de verificacao
 *
 * Flow:
 * 1. Receive 6-digit code and new password
 * 2. Find user by verification code
 * 3. Validate code expiration
 * 4. Hash new password
 * 5. Update user password
 * 6. Clear reset code
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ConfirmPasswordResetHandler {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    @Transactional
    public void handle(String code, String newPassword) {
        log.info("Password reset confirmation with code: {}", code);

        // Find user by reset code
        User user = userRepository.findByResetPasswordToken(code)
                .orElseThrow(() -> {
                    log.warn("Invalid password reset code");
                    return new ResponseStatusException(
                            HttpStatus.UNAUTHORIZED,
                            "Codigo invalido ou expirado"
                    );
                });

        // Validate code expiration
        if (!user.isResetPasswordTokenValid()) {
            log.warn("Expired password reset code for user: {}", user.getEmail().getValue());
            user.clearResetPasswordToken();
            userRepository.save(user);
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Codigo expirado. Solicite um novo reset de senha"
            );
        }

        // Hash new password
        String hashedPassword = passwordHasher.hash(newPassword);

        // Update user password
        user.setPassword(hashedPassword);

        // Clear reset code
        user.clearResetPasswordToken();

        userRepository.save(user);

        log.info("Password successfully reset for user: {}", user.getEmail().getValue());
    }
}
