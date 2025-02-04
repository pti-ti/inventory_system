package com.pti_sa.inventory_system.infrastructure.rest;

import com.pti_sa.inventory_system.application.UserService;
import com.pti_sa.inventory_system.domain.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
// http://localhost:8085
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Crear usuario
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        User createdUser = userService.saveUser(user);
        return ResponseEntity.ok(createdUser);
    }

    // Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<User>updateUser(@PathVariable Integer id, @RequestBody User user){
        user.setId(id);
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    // Obtener usuario por id
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id){
        Optional<User> user = userService.findUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    // Eliminar un usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteUser(@PathVariable Integer id){
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}
