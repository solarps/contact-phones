package com.testtask.phonecontacts.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtDecoder {
    private final JwtProperties properties;

    public DecodedJWT decodeJWT(String token) {
        return JWT.require(Algorithm.HMAC256(properties.getSecretKey()))
                .build()
                .verify(token);
    }
}
