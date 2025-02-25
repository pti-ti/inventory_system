package com.pti_sa.inventory_system.infrastructure.rest;

import com.pti_sa.inventory_system.application.dto.request.UserRequestDTO;
import com.pti_sa.inventory_system.infrastructure.jwt.JWTGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/security")
@Slf4j // Para ver que rol tiene el usuario autenticado.
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JWTGenerator jwtGenerator;

    public LoginController(AuthenticationManager authenticationManager, JWTGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserRequestDTO userRequestDTO){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequestDTO.email(), userRequestDTO.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Rol de User: {}", SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .findFirst()
                .get()
                .toString());

        String token = jwtGenerator.getToken(userRequestDTO.email());

        // ✅ Devolvemos un JSON válido
        Map<String, String> response = new HashMap<>();
        response.put("message", "Usuario logueado satisfactoriamente");
        response.put("token", "Bearer " + token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
