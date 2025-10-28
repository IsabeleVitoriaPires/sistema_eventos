package com.example.authservice.application.port;

public interface PasswordHasher {
    String hash(String password);
    boolean matches(String password, String hashedPassword);
}
