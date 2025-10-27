package com.example.authservice.application.auth;

import com.example.authservice.application.port.PasswordHasher;
import com.example.authservice.application.port.TokenService;
import com.example.authservice.domain.user.User;
import com.example.authservice.domain.user.UserRepository;
import com.example.authservice.domain.user.vo.Email;
import com.example.authservice.interfaces.rest.dto.auth.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordLoginHandler {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final TokenService tokenService;

    public TokenResponse handle(String rawEmail, String rawPassword) {
        Email email = Email.of(rawEmail);
        Optional<User> userOpt = userRepository.findByEmail(email.getValue());

        if (userOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais estão invalidas");
        }

        User user = userOpt.get();
        if (!passwordHasher.matches(rawPassword, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais estão invalidas");
        }

        TokenService.TokenPair pair = tokenService.issue(user);
        return new TokenResponse(
                pair.accessToken(),
                pair.refreshToken(),
                pair.expiresInSeconds()
        );
    }
}
