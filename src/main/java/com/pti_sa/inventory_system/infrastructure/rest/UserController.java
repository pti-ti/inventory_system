package com.pti_sa.inventory_system.infrastructure.rest;

import com.pti_sa.inventory_system.application.UserService;
import com.pti_sa.inventory_system.application.dto.response.UserResponseDTO;
import com.pti_sa.inventory_system.domain.model.User;
import com.pti_sa.inventory_system.infrastructure.mapper.UserMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
// http://localhost:8085
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    // Crear usuario
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }

    // Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Integer id, @RequestBody User user) {
        user.setId(id);
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(userMapper.toResponseDTO(updatedUser));
    }

    // Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO>getUserById(@PathVariable Integer id){
        return userService.getUserById(id)
                .map(userMapper::toResponseDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    //

    // Obtener los usuarios por Localidad
    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<UserResponseDTO>> getUsersByLocation(@PathVariable Integer locationId) {
        List<User> users = userService.getUsersByLocation(locationId);
        List<UserResponseDTO> response = users.stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // Buscar usuario por email
    @GetMapping("/by-email")
    public ResponseEntity<List<UserResponseDTO>> searchUsersByEmail(@RequestParam String email){
        List<UserResponseDTO> users = userService.findUserByEmail(email);
        return ResponseEntity.ok(users);
    }

    // Eliminar un usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("Usuario con ID " + id + " eliminado exitosamente.");

    }
}
