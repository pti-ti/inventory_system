package com.pti_sa.inventory_system.infrastructure.jwt;

import com.pti_sa.inventory_system.infrastructure.service.CustomUserDetailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static com.pti_sa.inventory_system.infrastructure.jwt.Constants.*;

public class JWTValidate {

    ///  Valida que el token venga en la petición
    public static boolean tokenExists(HttpServletRequest request, HttpServletResponse response){
        String header = request.getHeader(HEADER_AUTHORIZATION);
        if (header == null || !header.startsWith(TOKEN_BEARER_PREFIX))
            return false;
        return true;
    }

    // Valida que el token sea el correcto
    public static Claims JWTValid(HttpServletRequest request){
        String jwtToken = request.getHeader(HEADER_AUTHORIZATION).replace(TOKEN_BEARER_PREFIX, "");

        return Jwts.parserBuilder()
                .setSigningKey(JwtConfig.getSecretKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    // Autenticar al usuario en el flujo de spring
    public static void setAuthentication(Claims claims, CustomUserDetailService customUserDetailService){
        UserDetails userDetails = customUserDetailService.loadUserByUsername(claims.getSubject());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
