package com.example.authservice.application.user;

import com.example.authservice.application.port.PasswordHasher;
import com.example.authservice.domain.user.User;
import com.example.authservice.domain.user.UserRepository;
import com.example.authservice.domain.user.vo.Email;
import com.example.authservice.domain.user.vo.RoleType;
import com.example.authservice.interfaces.rest.dto.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class RegisterUserHandler {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    /**
     * Handle user registration
     * Requisito 3.2: Cadastro com email, senha, primeiro nome e sobrenome
     * Requisito 3.4: Validar email ja cadastrado
     */
    public UserResponse handle(String firstName, String lastName, String emailRaw, String passwordRaw, String roleRaw) {
        Email email = Email.of(emailRaw);

        // Requisito 3.4: Check for duplicate email
        if (userRepository.existsByEmail(email.getValue())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email ja cadastrado. Se voce ja possui uma conta, faca login em /auth/login/password"
            );
        }

        // Parse role from string to enum
        RoleType role;
        try {
            role = RoleType.valueOf(roleRaw.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Role inválido. Valores aceitos: USER, ORGANIZER"
            );
        }

        String hashedPassword = passwordHasher.hash(passwordRaw);
        User user = new User(firstName, lastName, email, role, hashedPassword);
        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail().getValue(),
                savedUser.getRole().getValue().name()
        );
    }
}
