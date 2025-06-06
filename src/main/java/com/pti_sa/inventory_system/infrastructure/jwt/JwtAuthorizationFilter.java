package com.pti_sa.inventory_system.infrastructure.jwt;

import com.pti_sa.inventory_system.infrastructure.service.CustomUserDetailService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.pti_sa.inventory_system.infrastructure.jwt.JWTValidate.*;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final CustomUserDetailService customUserDetailService;

    public JwtAuthorizationFilter(CustomUserDetailService customUserDetailService) {
        this.customUserDetailService = customUserDetailService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        System.out.println("🔍 Path solicitado: " + path);

        try {
            if (path.equals("/api/v1/security/login")) {
                System.out.println("⚠️ Ignorando token en login");
                filterChain.doFilter(request, response);
                return;
            }

            if (tokenExists(request, response)) {
                Claims claims = JWTValid(request);
                if (claims.get("authorities") != null) {
                    setAuthentication(claims, customUserDetailService);
                } else {
                    SecurityContextHolder.clearContext();
                }
            } else {
                SecurityContextHolder.clearContext();
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
            System.out.println("❌ Token inválido: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }

}
