package com.pti_sa.inventory_system.infrastructure.rest;

import com.pti_sa.inventory_system.application.dto.request.UserRequestDTO;
import com.pti_sa.inventory_system.infrastructure.jwt.JWTGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Controlador del Login", description = "Controlador del login para el acceso de los usuarios segun su rol")
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

    @Operation(
            summary = "Login de usuario",
            description = "Autentica el usuario con su correo electrónico y contraseña. Devuelve un token JWT, el tipo de usuario (rol) y un mensaje."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario autenticado correctamente y token generado."),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas. No autorizado."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
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

        String userType = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .findFirst()
                .get()
                .toString();

        log.info("Rol de User: {}", userType);

        String token = jwtGenerator.getToken(userRequestDTO.email());

        // ✅ Devolvemos un JSON válido
        Map<String, String> response = new HashMap<>();
        response.put("message", "Usuario logueado satisfactoriamente");
        response.put("token", "Bearer " + token);
        response.put("userType", userType);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
