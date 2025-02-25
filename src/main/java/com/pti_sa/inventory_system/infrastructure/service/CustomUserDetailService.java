package com.pti_sa.inventory_system.infrastructure.service;


import com.pti_sa.inventory_system.application.UserService;
import com.pti_sa.inventory_system.domain.model.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailService implements UserDetailsService {
    private UserService userService;

    public CustomUserDetailService(UserService userService){
        this.userService = userService;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Obtener el usuario desde la base de datos
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        // Asignar el rol desde la entidad User (suponiendo que 'role' es un enum)
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getUserType().name());

        // Retornar el UserDetails con el rol asignado
        return new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(authority) // Asignar el rol dinámicamente

        );
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        return userService.findByEmail(email)
//                .map(user -> new org.springframework.security.core.userdetails.User(
//                        user.getEmail(),
//                        user.getPassword(),
//                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")) // Asigna roles
//                ))
//                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));
    }
}
