package com.pti_sa.inventory_system.infrastructure.rest;

import com.pti_sa.inventory_system.application.UserService;
import com.pti_sa.inventory_system.application.dto.response.LogbookResponseDTO;
import com.pti_sa.inventory_system.application.dto.response.UserResponseDTO;
import com.pti_sa.inventory_system.domain.model.User;
import com.pti_sa.inventory_system.domain.model.UserType;
import com.pti_sa.inventory_system.infrastructure.mapper.UserMapper;
import com.pti_sa.inventory_system.infrastructure.service.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
// http://localhost:8085
@RequestMapping("/api/v1/admin/users")
//@RequestMapping("/api/v1/users")

public class UserController {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(BCryptPasswordEncoder passwordEncoder, UserService userService, UserMapper userMapper) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    // Crear usuario (ADMIN Y TECHNICIAN pueden usar este endpoint)
    @PreAuthorize("hasAnyRole('ADMIN' , 'TECHNICIAN')")
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encriptar de la clave
        return ResponseEntity.ok(userService.saveUser(user));
    }

    // Registrar Usuario
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        String defaultPassword = "123";
        user.setPassword(passwordEncoder.encode(defaultPassword));
        user.setUserType(UserType.USER);

        // Obtener usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado.");
        }

        Object principal = authentication.getPrincipal();
        Integer createdBy = null;
        if (principal instanceof CustomUserDetails) {
            createdBy = ((CustomUserDetails) principal).getId();
        }

        if (createdBy == null) {
            return ResponseEntity.badRequest().body("No se pudo obtener el usuario creador.");
        }

        // Crear auditoría antes de guardar el usuario
        user.createAudit(createdBy);

        try {
            UserResponseDTO savedUser = userService.registerUser(user);
            return ResponseEntity.ok(savedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    // Actualizar usuario (Solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Integer id, @RequestBody User user) {
        // Verificar si el usuario existe
        Optional<User> existingUser = userService.getUserById(id);
        if (existingUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User userToUpdate = existingUser.get();
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setLocation(user.getLocation());


        String authenticatedUserName  = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> authenticatedUser = userService.findByEmail(authenticatedUserName);
        userToUpdate.updateAudit(user.getUpdatedBy());

        // Obtener el id del usuario autenticado
        if (authenticatedUser.isPresent()) {
            Integer updatedById = authenticatedUser.get().getId();
            userToUpdate.updateAudit(updatedById);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User updatedUser = userService.updateUser(userToUpdate);

        return ResponseEntity.ok(userMapper.toResponseDTO(updatedUser));
    }


    // Eliminar usuario (Solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("Usuario con ID " + id + " eliminado exitosamente.");
    }

    // Obtener usuario por ID (ADMIN Y TECHNICIAN)
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO>getUserById(@PathVariable Integer id){
        return userService.getUserById(id)
                .map(userMapper::toResponseDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Obtener todos los usuarios (ADMIN Y TECHNICIAN)
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Object principal = authentication.getPrincipal();
        Integer requestedBy = null;

        if(principal instanceof CustomUserDetails){
            requestedBy = ((CustomUserDetails) principal).getId();
        }
        if (requestedBy == null){
            return ResponseEntity.badRequest().build();
        }

        List<UserResponseDTO> users = userService.findAllUsers();
        return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
    }

    // Buscar usuario por email (ADMIN Y TECHNICIAN)
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @GetMapping("/by-email")
    public ResponseEntity<List<UserResponseDTO>> searchUsersByEmail(@RequestParam String email){
        List<UserResponseDTO> users = userService.findUserByEmail(email);
        return ResponseEntity.ok(users);
    }

    // Obtener los usuarios por Localidad (ADMIN y TECHNICIAN)
    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<UserResponseDTO>> getUsersByLocation(@PathVariable Integer locationId) {
        List<User> users = userService.getUsersByLocation(locationId);
        List<UserResponseDTO> response = users.stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }




}

