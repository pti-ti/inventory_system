package com.pti_sa.inventory_system.infrastructure.rest;

import com.pti_sa.inventory_system.application.UserService;
import com.pti_sa.inventory_system.application.dto.response.UserResponseDTO;
import com.pti_sa.inventory_system.domain.model.User;
import com.pti_sa.inventory_system.domain.model.UserType;
import com.pti_sa.inventory_system.infrastructure.mapper.UserMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<UserResponseDTO> registerUser (@Valid @RequestBody User user){
        String defaultPassword = "123";
        String defaultUserType = "USER";
        user.setPassword(passwordEncoder.encode(defaultPassword));
        user.setUserType(UserType.USER);
        return ResponseEntity.ok(userService.registerUser(user));
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
        List<UserResponseDTO> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
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

