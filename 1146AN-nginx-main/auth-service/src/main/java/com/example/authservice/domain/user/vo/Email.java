package com.example.authservice.domain.user.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
public class Email {
    @jakarta.validation.constraints.Email
    @Column(name = "email")
    private String value;

    public Email() {}

    public Email(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("O email é obrigatorio");
        }

        this.value = normalize(value);
    }

    public static Email of(String value) {
        return new Email(value);
    }

    private static String normalize(String value) {
        return value == null ? null : value.trim().toLowerCase();
    }
}
