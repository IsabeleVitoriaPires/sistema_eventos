package com.example.authservice.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Setter
@Getter
public class JwtProperties {
    private String secret;
    private String issuer = "auth-service";
    private String audience = "pizza app";
    private long accessTtlSeconds = 900;
    private long refresTtlSeconds = 2_592_000;
}
