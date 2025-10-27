package com.example.authservice.domain.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    User save(User user);
    Optional<User> findById(UUID id);
    Page<User> findAll(Pageable pageable);
}
