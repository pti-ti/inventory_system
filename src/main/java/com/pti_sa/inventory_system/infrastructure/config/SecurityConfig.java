package com.pti_sa.inventory_system.infrastructure.config;

import com.pti_sa.inventory_system.infrastructure.jwt.JwtAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    public SecurityConfig(JwtAuthorizationFilter jwtAuthorizationFilter) {
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // 🔓 Rutas públicas de frontend
                        .requestMatchers("/", "/index.html", "/assets/**", "/favicon.ico").permitAll()
                        .requestMatchers("/**/*.js", "/**/*.css").permitAll()

                        // 🔓 Endpoints públicos
                        .requestMatchers("/auth/login", "/api/v1/security/login").permitAll()
                        .requestMatchers("/dashboard").permitAll()
                        .requestMatchers("/error").permitAll()

                        // 🔓 Documentación Swagger
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // 🔓 Estadísticas públicas
                        .requestMatchers(
                                "/api/v1/locations/device-location-count",
                                "/api/v1/status/device-status-count",
                                "/api/v1/locations/device-location-type-count",
                                "/api/v1/devices/count-by-type",
                                "/api/v1/devices/total-inventory-value"
                        ).permitAll()

                        // ⚠️ Crear primer usuario (luego protegerlo)
                        .requestMatchers("/api/v1/admin/users/create").permitAll()
                        // Para producción, cámbialo por:
                        // .requestMatchers("/api/v1/admin/users/create").hasRole("ADMIN")

                        // 🔒 Rutas para ADMIN y TECHNICIAN
                        .requestMatchers(
                                "/api/v1/devices",
                                "/api/v1/devices/**",
                                "/api/v1/logbooks/register",
                                "/api/v1/maintenances/register",
                                "/api/v1/locations",
                                "/api/v1/locations/**",
                                "/api/v1/status",
                                "/api/v1/status/**",
                                "/api/v1/brands",
                                "/api/v1/brands/**"
                        ).hasAnyRole("ADMIN", "TECHNICIAN")

                        // 🔒 Rutas solo para ADMIN
                        .requestMatchers(
                                "/api/v1/admin/users",
                                "/api/v1/admin/users/register",
                                "/api/v1/admin/items/**",
                                "/api/v1/admin/**"
                        ).hasRole("ADMIN")

                        // 🔒 Cualquier otra ruta requiere autenticación
                        .anyRequest().authenticated()
                )
                .addFilterAfter(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOriginPatterns(List.of(
                "http://localhost:5173",
                "http://192.168.128.148:*"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
