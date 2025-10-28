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

    public UserResponse handle(String name, String emailRaw, String passwordRaw) {
        Email email = Email.of(emailRaw);

        if (userRepository.existsByEmail(email.getValue())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado");
        }

        String hashedPassword = passwordHasher.hash(passwordRaw);
        User user = new User(name, email, RoleType.USER, hashedPassword);
        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail().getValue(),
                savedUser.getRole().getValue().name()
        );
    }
}
