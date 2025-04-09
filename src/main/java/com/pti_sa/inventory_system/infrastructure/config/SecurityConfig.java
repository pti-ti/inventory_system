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

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    public SecurityConfig(JwtAuthorizationFilter jwtAuthorizationFilter) {
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Habilitar CORS correctamente
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(aut -> aut

                        .requestMatchers("/api/v1/admin/users/create").permitAll()
                        .requestMatchers("/api/v1/admin/locations/create").permitAll()
                        .requestMatchers("/api/v1/admin/status/device-status-count").permitAll()
                        .requestMatchers("/api/v1/admin/locations/device-location-count").permitAll()
                        .requestMatchers("/api/v1/admin/locations/device-location-type-count").permitAll()
                        .requestMatchers("/api/v1/admin/devices/count-by-type").permitAll()
                        .requestMatchers("/api/v1/admin/devices/total-inventory-value").permitAll()


                        // 🔹 Rutas accesibles solo por ADMIN
                        .requestMatchers("/api/v1/admin/items/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN") // Esta línea ya cubre todas las rutas admin

                        // 🔹 Rutas accesibles por ADMIN y TECHNICIAN
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

                        // Rutas públicas
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/assets/**",
                                "/favicon.ico",
                                "/auth/login",
                                "/dashboard",
                                "/**/*.js",
                                "/**/*.css"
                        ).permitAll()
                        .requestMatchers("/api/v1/security/login").permitAll()
                        .requestMatchers("/api/v1/locations/create").permitAll()
                        .requestMatchers("/api/v1/status/create").permitAll()

                        //.requestMatchers("/api/v1/users/create").permitAll() activar para crear un admin
                        .requestMatchers("/error").permitAll()

                        // 🔹 Cualquier otra petición requiere autenticación
                        .anyRequest().authenticated()
                )
                .addFilterAfter(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        org.springframework.web.cors.CorsConfiguration config = new org.springframework.web.cors.CorsConfiguration();

        //config.addAllowedOrigin("http://localhost:5173"); // Frontend permitidos

        config.setAllowedOriginPatterns(List.of(
                "http://localhost:5173",
                "http://192.168.128.148:*"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        source.registerCorsConfiguration("/**", config);
        return source;

    }
}
