package com.example.authservice.domain.user.vo;

import lombok.Getter;

/**
 * Roles do sistema de compra de ingressos
 * USER: Usuário comum que compra ingressos
 * ORGANIZER: Organizador de eventos
 * ADMIN: Administrador do sistema
 */
@Getter
public enum RoleType {
    USER(1),
    ORGANIZER(2),
    ADMIN(3);

    private final int level;

    RoleType(int level) {
        this.level = level;
    }

    public boolean covers(RoleType other) {
        return this.level >= other.level;
    }
}
