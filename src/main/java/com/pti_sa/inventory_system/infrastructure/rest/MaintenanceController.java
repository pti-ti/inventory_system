package com.pti_sa.inventory_system.infrastructure.rest;

import com.pti_sa.inventory_system.application.MaintenanceService;
import com.pti_sa.inventory_system.application.dto.response.MaintenanceResponseDTO;
import com.pti_sa.inventory_system.domain.model.Item;
import com.pti_sa.inventory_system.domain.model.Maintenance;
import com.pti_sa.inventory_system.infrastructure.service.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Tag(name = "Mantenimientos", description = "Controlador para gestionar las bitácoras de mantenimiento")
@RequestMapping("/api/v1/maintenances")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    public MaintenanceController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    @Operation(summary = "Registrar nuevo mantenimiento", description = "Permite registrar una nueva bitácora de mantenimiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mantenimiento registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })

    // Crear Mantenimiento
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @PostMapping("/register")
    public ResponseEntity<?> createMaintenance (@RequestBody Maintenance maintenance){
        System.out.println("Mantenimiento recibido: " + maintenance);

        if(maintenance.getDevice() == null || maintenance.getDevice().getId() == null){
            System.out.println("Error: El dispositivo es requerido.");

            return ResponseEntity.badRequest().body("El dispositivo es requerido.");
        }

        // Obtener usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()){
            System.out.println("Error: Usuario no autenticado");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado.");
        }

        Object principal = authentication.getPrincipal();
        Integer createdBy = null;

        if(principal instanceof CustomUserDetails){
            createdBy = ((CustomUserDetails) principal).getId();
        }

        if(createdBy == null){
            System.out.println("Error: No se pudo obtener el usuario creador");

            return ResponseEntity.badRequest().body("No se pudo obtener el usuario creador.");
        }

        System.out.println("Usuario autenticado ID: " + createdBy);

        //Crear auditoría
        maintenance.createAudit(createdBy);

        try{
            MaintenanceResponseDTO savedMaintenance = maintenanceService.saveMaintenance(maintenance);
            System.out.println("Bitácora registrada con éxito: " + savedMaintenance);

            return ResponseEntity.ok(savedMaintenance);
        }catch (IllegalArgumentException e){
            System.out.println("Error al guardar la bitácora: " + e.getMessage());

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Actualizar mantenimiento", description = "Actualiza los datos de una bitácora de mantenimiento existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mantenimiento actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
            @ApiResponse(responseCode = "401", description = "No autenticado")
    })

    // Actualizar mantenimiento
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMaintenance(@PathVariable Integer id, @RequestBody Maintenance maintenance) {
        maintenance.setId(id);

        //Obtener usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()) {
            System.out.println("Error: Usuario no autenticado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado.");

        }

        Object principal = authentication.getPrincipal();
        Integer updatedBy = null;


        if (principal instanceof CustomUserDetails) {
            updatedBy = ((CustomUserDetails) principal).getId();
        }

        if (updatedBy == null) {
            System.out.println(" Error: No se pudo obtener el usuario autenticado.");
            return ResponseEntity.badRequest().body("No se pudo obtener el usuario autenticado.");
        }

        System.out.println(" Usuario autenticado para updateBy: " + updatedBy);

        // Asignar el usuario autenticado
        maintenance.setUpdatedBy(updatedBy);

        // Llamar al servicio para actualizar
        MaintenanceResponseDTO updatedMaintenance = maintenanceService.updateMaintenance(maintenance);
        return ResponseEntity.ok(updatedMaintenance);

    }

    @Operation(summary = "Obtener mantenimientos por ID de dispositivo", description = "Devuelve todas las bitácoras asociadas a un dispositivo específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de mantenimientos encontrado")
    })

    // Obtener Mantenimiento por ID del dispositivo
    @GetMapping("/device/{id}")
    public ResponseEntity<List<MaintenanceResponseDTO>> getMaintenancesByDeviceId(@PathVariable Integer deviceId) {
        List<MaintenanceResponseDTO> maintenances = maintenanceService.findMaintenancesByDeviceId(deviceId);
        return ResponseEntity.ok(maintenances);
    }

    // Obtener mantenimiento por ID de bitácora
    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceResponseDTO> getMaintenanceById(@PathVariable Integer id){
        return maintenanceService.findMaintenanceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar todos los mantenimientos", description = "Devuelve una lista de todas las bitácoras registradas")
    @ApiResponse(responseCode = "200", description = "Listado completo de mantenimientos")

    // Obtener todos los mantenimientos
    @GetMapping
    public ResponseEntity<List<MaintenanceResponseDTO>> getAllMaintenances() {
        List<MaintenanceResponseDTO> maintenances = maintenanceService.findAllMaintenances();
        return ResponseEntity.ok(maintenances);
    }

    @Operation(summary = "Obtener ítems por ID de mantenimiento", description = "Devuelve los ítems asociados a una bitácora de mantenimiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ítems encontrados exitosamente"),
            @ApiResponse(responseCode = "404", description = "Bitácora no encontrada")
    })
    @GetMapping("/{id}/items")
    public ResponseEntity<?> getItemsByMaintenanceId(@PathVariable Integer id) {
        try {
            List<Item> items = maintenanceService.findItemsByMaintenanceId(id);
            return ResponseEntity.ok(items);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @Operation(summary = "Buscar mantenimientos por tipo", description = "Devuelve los mantenimientos registrados según su tipo")
    @ApiResponse(responseCode = "200", description = "Mantenimientos encontrados")

    // Obtener los mantenimientos por tipo
    @GetMapping("/type/{type}")
    public ResponseEntity<List<MaintenanceResponseDTO>> getMaintenancesBYType(@PathVariable String type){
        List<MaintenanceResponseDTO> maintenances = maintenanceService.findMaintenancesByType(type);
        return ResponseEntity.ok(maintenances);
    }

    @Operation(summary = "Buscar por rango de fechas", description = "Obtiene todas las bitácoras dentro del rango de fechas dado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado filtrado por fechas")
    })

    // Buscar Mantenimientos por rango de fechas
    @GetMapping("/by-date-range")
    public ResponseEntity<List<MaintenanceResponseDTO>> getMaintenancesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){

        List<MaintenanceResponseDTO> maintenances = maintenanceService.findMaintenancesByDateRange(startDate, endDate);
        return ResponseEntity.ok(maintenances);
    }


    @Operation(summary = "Eliminar mantenimiento", description = "Elimina una bitácora por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Eliminado exitosamente")
    })

    // Eliminar un mantenimiento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenance(@PathVariable Integer id) {
        maintenanceService.deleteMaintenanceById(id);
        return ResponseEntity.noContent().build();
    }
}
