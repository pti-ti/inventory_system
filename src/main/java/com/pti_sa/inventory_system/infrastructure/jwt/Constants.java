package com.pti_sa.inventory_system.infrastructure.jwt;


import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;

public class Constants {
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String TOKEN_BEARER_PREFIX = "Bearer ";

    public static final String SUPER_SECRET_KEY = "G7zQmYtR9Xv2pLwKdCfB6sNhJ53VAZq8TXMKPYgWJDLF4cMZVtHfN9Bk7añlkdfjalkdfjañlkfj432k42hlkgHFAJLWO23Rx8GTy";
    public static final long TOKEN_EXPIRATION_TIME = 1500000; // 15 MINUTOS de validación del token

    public static Key getSignedKey(String secretKey){
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
