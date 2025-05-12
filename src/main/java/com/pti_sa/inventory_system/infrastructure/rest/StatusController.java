package com.pti_sa.inventory_system.infrastructure.rest;

import com.pti_sa.inventory_system.application.StatusService;
import com.pti_sa.inventory_system.application.dto.response.LocationResponseDTO;
import com.pti_sa.inventory_system.application.dto.response.StatusResponseDTO;
import com.pti_sa.inventory_system.domain.model.Location;
import com.pti_sa.inventory_system.domain.model.Status;
import com.pti_sa.inventory_system.infrastructure.service.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Tag(name = "Estados", description = "Operaciones relacionadas con los estados de los dispositivos")
@RequestMapping("/api/v1/status")
public class StatusController {

    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @Operation(summary = "Crear un nuevo estado", description = "Permite crear un estado si el usuario está autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o usuario no válido"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
    // Crear un nuevo Status
    @PostMapping("/create")
    public ResponseEntity<StatusResponseDTO> createStatus(@RequestBody Status status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Object principal = authentication.getPrincipal();
        Integer createdBy = null;
        if (principal instanceof CustomUserDetails) {
            createdBy = ((CustomUserDetails) principal).getId();
        }

        if (createdBy == null) {
            return ResponseEntity.badRequest().build();
        }

        StatusResponseDTO savedStatus = statusService.saveStatus(status, createdBy);
        return ResponseEntity.ok(savedStatus);
    }

    @Operation(summary = "Actualizar un estado", description = "Actualiza la información de un estado existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o usuario no válido"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
    // Actualizar un Status
    @PutMapping("/{id}")
    public ResponseEntity<Status> updateStatus(@PathVariable Integer id, @RequestBody Status status) {
        // Obtener usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Object principal = authentication.getPrincipal();
        Integer updatedBy = null;
        if (principal instanceof CustomUserDetails) {
            updatedBy = ((CustomUserDetails) principal).getId();
        }

        if (updatedBy == null) {
            return ResponseEntity.badRequest().build();
        }

        // Asignar el ID del usuario que está actualizando
        status.setId(id);
        status.setUpdatedBy(updatedBy);

        Status updatedStatus = statusService.updateStatus(status);
        return ResponseEntity.ok(updatedStatus);
    }

    @Operation(summary = "Obtener estado por ID", description = "Devuelve los detalles de un estado específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado encontrado"),
            @ApiResponse(responseCode = "404", description = "Estado no encontrado")
    })
    // Obtener Status por ID
    @GetMapping("/{id}")
    public ResponseEntity<StatusResponseDTO>getStatusById(@PathVariable Integer id){
        Optional<StatusResponseDTO> status = statusService.findStatusById(id);
        return status.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener todos los estados", description = "Devuelve una lista de todos los estados registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de estados obtenida exitosamente")
    })
    // Obtener todos los Status
    @GetMapping
    public ResponseEntity<List<StatusResponseDTO>> getAllStatuses(){
        List<StatusResponseDTO> statuses = statusService.findAllStatuses();
        return ResponseEntity.ok(statuses);
    }

    @Operation(summary = "Obtener cantidad de dispositivos por estado", description = "Retorna un mapa con el nombre del estado y la cantidad de dispositivos asociados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conteo obtenido exitosamente")
    })
    // Obtener la cantidad de dispositivos por estado
    @GetMapping("/device-status-count")
    public ResponseEntity<Map<String, Long>> getDeviceCountByStatus() {
        Map<String, Long> statusCount = statusService.countDevicesByStatus();
        return ResponseEntity.ok(statusCount);
    }

    @Operation(summary = "Eliminar un estado por ID", description = "Elimina un dispositivo.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Estado eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Estado no encontrado")
    })
    // Eliminar un Status
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatus(@PathVariable Integer id){
        statusService.deleteStatusById(id);
        return ResponseEntity.noContent().build();
    }
}
