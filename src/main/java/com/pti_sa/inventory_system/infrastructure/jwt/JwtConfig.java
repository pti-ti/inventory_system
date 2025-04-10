package com.pti_sa.inventory_system.infrastructure.jwt;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.util.Base64;


@Configuration
public class JwtConfig {

    @Value("${jwt.secret:}")
    private String secretFromConfig;

    @Getter
    private static SecretKey secretKey;

    @PostConstruct
    public void init() {
        System.out.println("🧪 JWT Secret recibido: '" + secretFromConfig + "'");

        if (secretFromConfig == null || secretFromConfig.isBlank()) {
            throw new IllegalStateException("❌ La propiedad 'jwt.secret' está vacía o no se pudo leer.");
        }

        if (secretFromConfig.length() < 32) {
            throw new IllegalStateException("❌ La clave JWT debe tener al menos 32 caracteres.");
        }

        // ¡Sin codificar en Base64!
        secretKey = Keys.hmacShaKeyFor(secretFromConfig.getBytes());

        System.out.println("✅ Clave JWT inicializada correctamente.");
    }

}

