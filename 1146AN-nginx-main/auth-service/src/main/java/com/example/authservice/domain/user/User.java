package com.example.authservice.domain.user;

import com.example.authservice.domain.user.vo.Email;
import com.example.authservice.domain.user.vo.Role;
import com.example.authservice.domain.user.vo.RoleType;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Table(name = "usuario")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String password;

    @Valid
    @Embedded
    private Email email;

    @Embedded
    private Role role;

    @Column
    private String resetPasswordToken;

    @Column
    private Long resetPasswordTokenExpiry;

    public User(String firstName, String lastName, @Valid Email email, RoleType role, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = Role.of(role);
        this.password = password;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean isResetPasswordTokenValid() {
        if (resetPasswordToken == null || resetPasswordTokenExpiry == null) {
            return false;
        }
        return System.currentTimeMillis() < resetPasswordTokenExpiry;
    }

    public void clearResetPasswordToken() {
        this.resetPasswordToken = null;
        this.resetPasswordTokenExpiry = null;
    }

    public void promoteToOrganizer() {
        this.role = Role.of(RoleType.ORGANIZER);
    }
}
