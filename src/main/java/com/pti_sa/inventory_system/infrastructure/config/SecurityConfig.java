package com.pti_sa.inventory_system.infrastructure.config;

import com.pti_sa.inventory_system.infrastructure.jwt.JwtAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // 🔥 Habilitar CORS aquí
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(aut -> aut
                        .requestMatchers("/api/v1/admin/users").hasAnyRole("ADMIN", "TECHNICIAN")
                        .requestMatchers("/api/v1/admin/users/register").hasAnyRole("ADMIN", "TECHNICIAN")
                        .requestMatchers("/api/v1/admin/devices/register").hasAnyRole("ADMIN", "TECHNICIAN")
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/security/login").permitAll()
                        //.requestMatchers("/api/v1/admin/devices/register").permitAll()
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated()

                        // Rutas accesibles por ADMIN y TECHNICIAN ( buscar y crear usuarios)

                        // Permitir que cualquiera (sin autenticación) pueda crear usuarios
                        //.requestMatchers("/api/v1/users").permitAll()
                        // Ruta pública para login

                        // Cualquier otra petición requiere autenticación

        ).addFilterAfter(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);


        return httpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource(){
        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        org.springframework.web.cors.CorsConfiguration config = new org.springframework.web.cors.CorsConfiguration();

        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);

        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
