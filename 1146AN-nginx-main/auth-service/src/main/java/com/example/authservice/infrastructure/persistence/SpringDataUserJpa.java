package com.example.authservice.infrastructure.persistence;

import com.example.authservice.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataUserJpa extends JpaRepository<User, UUID> {
    Optional<User> findByEmail_value(String email);
    boolean existsByEmail_value(String email);
}
