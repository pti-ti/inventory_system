package com.pti_sa.inventory_system.infrastructure.rest;

import com.pti_sa.inventory_system.application.LocationService;
import com.pti_sa.inventory_system.application.UserService;
import com.pti_sa.inventory_system.application.dto.response.LocationResponseDTO;
import com.pti_sa.inventory_system.application.dto.response.LogbookResponseDTO;
import com.pti_sa.inventory_system.application.dto.response.UserResponseDTO;
import com.pti_sa.inventory_system.domain.model.Location;
import com.pti_sa.inventory_system.domain.model.User;
import com.pti_sa.inventory_system.domain.model.UserType;
import com.pti_sa.inventory_system.infrastructure.mapper.LocationMapper;
import com.pti_sa.inventory_system.infrastructure.mapper.UserMapper;
import com.pti_sa.inventory_system.infrastructure.service.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Usuarios", description = "Operaciones relacionadas con los usuarios del inventario")
// http://localhost:8085
@RequestMapping("/api/v1/users")
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

    @Operation(summary = "Crea un nuevo usuario", description = "Crea un usuario con constraseña encriptada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    //@PreAuthorize("hasAnyRole('ADMIN' , 'TECHNICIAN')")
    @PostMapping("/create")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encriptar de la clave
        return ResponseEntity.ok(userService.saveUser(user));
    }

    @Operation(summary = "Registrar un nuevo usuario", description = "Registra un usuario con rol User y contraseña predeterminada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "400", description = "Error de validación o lógica")
    })
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

    @Operation(summary = "Actualizar usuario", description = "Actualiza un usuario existente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
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


    @Operation(summary = "Eliminar un usuario", description = "Elimina un usuario existente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    // Eliminar usuario (Solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("Usuario con ID " + id + " eliminado exitosamente.");
    }

    @Operation(summary = "Obtener usuario por ID", description = "Retorna los datos de un usuario por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    // Obtener usuario por ID (ADMIN Y TECHNICIAN)
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO>getUserById(@PathVariable Integer id){
        return userService.getUserById(id)
                .map(userMapper::toResponseDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar todos los usuarios", description = "Obtiene la lista de todos los usuarios registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente"),
            @ApiResponse(responseCode = "204", description = "No hay usuarios para mostrar"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
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

    @Operation(summary = "Buscar usuarios por email", description = "Busca uno o más usuarios que coincidan con el mail proporcionado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios encotrados"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    // Buscar usuario por email (ADMIN Y TECHNICIAN)
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @GetMapping("/by-email")
    public ResponseEntity<List<UserResponseDTO>> searchUsersByEmail(@RequestParam String email){
        List<UserResponseDTO> users = userService.findUserByEmail(email);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Obtener usuarios por localidad", description = "Obtiene una lista de usuarios asignados a una localidad específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios obtenidos exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron usuarios para la localidad")
    })
    // Obtener los usuarios por Localidad (ADMIN y TECHNICIAN)
    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<UserResponseDTO>> getUsersByLocation(@PathVariable Integer locationId) {
        List<User> users = userService.getUsersByLocation(locationId);
        List<UserResponseDTO> response = users.stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar usuarios excepto los que tienen rol USER", description = "Obtiene todos los usuarios cuyo rol no es USER y que no están marcados como eliminados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios obtenidos exitosamente"),
            @ApiResponse(responseCode = "204", description = "No hay usuarios que cumplan con el criterio"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })

    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @GetMapping("/exclude-user-role")
    public ResponseEntity<List<UserResponseDTO>> getUsersExcludingUserRole() {
        List<UserResponseDTO> users = userService.findAllUserExcludingUserType();

        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(users);
    }
}

