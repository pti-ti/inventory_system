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
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

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

                        // 🔓 Rutas públicas (sin autenticación)
                        .requestMatchers("/", "/index.html", "/assets/**", "/favicon.ico", "/auth/login", "/dashboard").permitAll()
                        .requestMatchers("/**/*.js", "/**/*.css").permitAll()
                        .requestMatchers("/api/v1/security/login").permitAll()
                        .requestMatchers("/api/v1/locations/create").permitAll()
                        .requestMatchers("/api/v1/status/create").permitAll()
                        .requestMatchers("/error").permitAll()

                        // ⚠️ Ruta abierta solo temporalmente para crear el primer usuario
                        .requestMatchers("/api/v1/admin/users/create").permitAll()
                        // 🔒 Luego puedes comentar la línea de arriba y dejar solo esta para protegerla:
                        // .requestMatchers("/api/v1/admin/users/create").hasRole("ADMIN")

                        // 🔓 Datos estadísticos que no requieren autenticación
                        .requestMatchers(
                                "/api/v1/admin/locations/create",
                                "/api/v1/admin/status/device-status-count",
                                "/api/v1/admin/locations/device-location-count",
                                "/api/v1/admin/locations/device-location-type-count",
                                "/api/v1/admin/devices/count-by-type",
                                "/api/v1/admin/devices/total-inventory-value"
                        ).permitAll()

                        // 🔒 Rutas solo para ADMIN
                        .requestMatchers("/api/v1/admin/items/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")

                        // 🔒 Rutas compartidas por ADMIN y TECHNICIAN
                        .requestMatchers(
                                "/api/v1/admin/users",
                                "/api/v1/admin/users/register",
                                "/api/v1/admin/devices",
                                "/api/v1/admin/devices/**",
                                "/api/v1/admin/devices/register",
                                "/api/v1/admin/logbooks/register",
                                "/api/v1/admin/maintenances/register",
                                "/api/v1/admin/locations",
                                "/api/v1/admin/locations/**",
                                "/api/v1/admin/status",
                                "/api/v1/admin/status/**",
                                "/api/v1/admin/brands",
                                "/api/v1/admin/brands/**"
                        ).hasAnyRole("ADMIN", "TECHNICIAN")

                        // 🔒 Cualquier otra petición requiere autenticación
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
