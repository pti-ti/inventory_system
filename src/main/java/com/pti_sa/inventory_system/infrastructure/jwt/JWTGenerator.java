package com.pti_sa.inventory_system.infrastructure.jwt;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.pti_sa.inventory_system.infrastructure.jwt.Constants.*;

@Service
public class JWTGenerator {
    public String getToken(String username){
        // Obtener todas las autoridades del usuario autenticado
        List<GrantedAuthority> authorityList = new ArrayList<>(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities());

        // Construir el token JWT
        String token = Jwts.builder()
                .setId("inventory")
                .setSubject(username)
                .claim("authorities", authorityList.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .signWith(getSignedKey(SUPER_SECRET_KEY), SignatureAlgorithm.HS512)
                .compact();

        return "Bearer "+token;
    }
}
