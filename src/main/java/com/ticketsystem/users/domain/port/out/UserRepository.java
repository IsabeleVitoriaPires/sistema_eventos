package com.ticketsystem.users.domain.port.out;

import com.ticketsystem.users.domain.model.User;

import java.util.Optional;

/**
 * Porta de saída para persistência de usuários
 */
public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByResetPasswordToken(String token);

    boolean existsByEmail(String email);

    void deleteById(Long id);
}
