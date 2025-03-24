package com.pti_sa.inventory_system.infrastructure.jwt;


import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

public class Constants {
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String TOKEN_BEARER_PREFIX = "Bearer ";

    private static final String SECRET_STRING = "EstaEsUnaClaveSecretaSuperSeguraConMasDe64CaracteresParaJWT!!!";
    public static final SecretKey SUPER_SECRET_KEY = Keys.hmacShaKeyFor(
            Base64.getEncoder().encode(SECRET_STRING.getBytes())
    );
    public static final long TOKEN_EXPIRATION_TIME = 3600000; // 60 MINUTOS de validación del token

    public static Key getSignedKey(String secretKey){
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
