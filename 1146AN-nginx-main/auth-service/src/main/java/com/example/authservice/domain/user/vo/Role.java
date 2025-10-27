package com.example.authservice.domain.user.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class Role {

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private RoleType value;

    private Role(RoleType value) {
        if (value == null) {
            throw new IllegalArgumentException("Role obrigatória");
        }

        this.value = value;
    }

    public static Role of(RoleType value) {
        return new Role(value);
    }

    public boolean covers(RoleType other) {
        return this.value.covers(other);
    }
}
