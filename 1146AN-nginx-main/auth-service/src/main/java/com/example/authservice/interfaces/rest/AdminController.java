package com.example.authservice.interfaces.rest;

import com.example.authservice.application.user.PromoteUserToOrganizerHandler;
import com.example.authservice.interfaces.rest.dto.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminController {

    private final PromoteUserToOrganizerHandler promoteUserToOrganizerHandler;

    /**
     * Endpoint administrativo para promover um usuário a ORGANIZER
     * POST /admin/users/{userId}/promote-to-organizer
     *
     * Exemplo de uso:
     * POST http://localhost:8084/admin/users/a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6/promote-to-organizer
     *
     * Retorna: UserResponse com role atualizado para ORGANIZER
     */
    @PostMapping("/{userId}/promote-to-organizer")
    public ResponseEntity<UserResponse> promoteToOrganizer(@PathVariable UUID userId) {
        UserResponse response = promoteUserToOrganizerHandler.handle(userId);
        return ResponseEntity.ok(response);
    }
}
