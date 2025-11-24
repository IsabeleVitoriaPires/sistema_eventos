package com.example.authservice.interfaces.rest;


import com.example.authservice.application.user.ListUsersHandler;
import com.example.authservice.application.user.RegisterUserHandler;
import com.example.authservice.interfaces.rest.dto.user.RegisterUserRequest;
import com.example.authservice.interfaces.rest.dto.user.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final ListUsersHandler listUsersHandler;
    private final RegisterUserHandler registerUserHandler;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> list(Pageable pageable) {
        Page<UserResponse> page = listUsersHandler.handle(pageable);

        return ResponseEntity.ok(page);
    }

    /**
     * Requisito 3.2: Register new user
     * POST /users
     * Body: { "firstName", "lastName", "email", "password", "role" }
     */
    @PostMapping
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        UserResponse created = registerUserHandler.handle(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.password(),
                request.role()
        );

        return ResponseEntity.created(URI.create("/users/" + created.id())).body(created);
    }
}
