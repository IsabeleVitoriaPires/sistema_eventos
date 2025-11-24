package com.example.authservice.domain.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * User Repository Port (Interface)
 * Defines the contract for user persistence
 * Implementation will be in the infrastructure layer
 */
public interface UserRepository {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    Optional<User> findByResetPasswordToken(String token);
    User save(User user);
    Optional<User> findById(UUID id);
    Page<User> findAll(Pageable pageable);
}
