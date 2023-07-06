package com.testtask.phonecontacts.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties properties;

    public String assemble(Long userId, String username, List<String> roles) {
        return JWT.create()
                .withSubject(userId.toString())
                .withExpiresAt(Instant.now().plus(Duration.ofHours(1)))
                .withClaim("u", username)
                .withClaim("r", roles)
                .sign(Algorithm.HMAC256(properties.getSecretKey()));
    }
}
