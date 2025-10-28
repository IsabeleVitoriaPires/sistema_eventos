package com.example.authservice.domain.auth;

import com.example.authservice.domain.auth.vo.ExpiresAt;
import com.example.authservice.domain.auth.vo.HashedToken;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Table(name = "magic_link")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class MagicLink {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Embedded
    private HashedToken hashedToken;

    @Embedded
    private ExpiresAt expiresAt;

    @Column
    private Instant consumedAt;

    public MagicLink(UUID userId, HashedToken hashedToken, ExpiresAt expiresAt) {
        if (userId == null) throw new IllegalArgumentException("userId é obrigatorio");
        if (hashedToken == null) throw new IllegalArgumentException("hashedToken é obrigatorio");
        if (expiresAt == null) throw new IllegalArgumentException("expiresAt é obrigatorio");

        this.userId = userId;
        this.hashedToken = hashedToken;
        this.expiresAt = expiresAt;
    }

    public static MagicLink issueForLogin(UUID userId, HashedToken hashedToken, ExpiresAt expiresAt) {
        return new MagicLink(userId, hashedToken, expiresAt);
    }

    public boolean isExpired(Instant now) {
        return now.isAfter(this.expiresAt.getValue());
    }

    public boolean isConsumed() {
        return this.consumedAt != null;
    }

    public void consume(Instant now) {
        if (this.isConsumed()) throw new IllegalStateException("Link já consumido");
        if (this.isExpired(now)) throw new IllegalStateException("Link já expirado");

        this.consumedAt = now;
    }
}
