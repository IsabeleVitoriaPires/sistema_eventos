package com.example.gateway_service.infastructure.config;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.gateway_service.domain.user.vo.RoleType;

import reactor.core.publisher.Mono;

@Component
public class AuthorizationFilter implements WebFilter {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private static final Map<String, RoleType> routeRole = Map.of(
        "/demo1/waiter", RoleType.WAITER,
        "/demo1/customer", RoleType.CUSTOMER
    );

    private boolean isAuthorized(String path, RoleType role) {
        for (Map.Entry<String, RoleType> entry : routeRole.entrySet()) {
            if (path.startsWith(entry.getKey())) {
                return role.covers(entry.getValue());
            }
        }

        return true;
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().toString();

        // verifica se a rota existe na nossa lista de rotas protegidas
        if (routeRole.entrySet().stream().noneMatch(entry -> path.startsWith(entry.getKey()))) {
            return chain.filter(exchange);
        }

        // Verifica se o token existe no header Authorization e se ele inicia com "Bearer "
        String authHeader = request.getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange);
        }

        String token = authHeader.substring(7);
        DecodedJWT jwt;
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));
            JWTVerifier verifier = JWT.require(algorithm).build();
            jwt = verifier.verify(token);
        } catch (Exception e) {
            return unauthorized(exchange);
        }

        String tokenType = jwt.getClaim("type").asString();
        if (!tokenType.equals("access")) {
            return unauthorized(exchange);
        }

        String usrRoleType = jwt.getClaim("role").asString();
        RoleType role = null;
        try {
            role = RoleType.valueOf(usrRoleType);
        } catch (Exception e) {
            return unauthorized(exchange);
        }

        if (!isAuthorized(path, role)) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }
}
