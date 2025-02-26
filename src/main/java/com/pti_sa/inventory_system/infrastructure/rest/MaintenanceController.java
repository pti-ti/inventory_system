package com.pti_sa.inventory_system.infrastructure.rest;

import com.pti_sa.inventory_system.application.MaintenanceService;
import com.pti_sa.inventory_system.application.dto.response.MaintenanceResponseDTO;
import com.pti_sa.inventory_system.domain.model.Maintenance;
import com.pti_sa.inventory_system.infrastructure.service.CustomUserDetails;
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
@RequestMapping("/api/v1/admin/maintenances")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    public MaintenanceController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

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
    /*@PostMapping
    public ResponseEntity<MaintenanceResponseDTO> createMaintenance(@RequestBody Maintenance maintenance) {
        MaintenanceResponseDTO createdMaintenance = maintenanceService.saveMaintenance(maintenance);
        return ResponseEntity.ok(createdMaintenance);
    }*/

        // Actualizar mantenimiento
    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceResponseDTO> updateMaintenance(@PathVariable Integer id, @RequestBody Maintenance maintenance) {
        maintenance.setId(id);
        MaintenanceResponseDTO updatedMaintenance = maintenanceService.updateMaintenance(maintenance);
        return ResponseEntity.ok(updatedMaintenance);
    }

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

    // Obtener todos los mantenimientos
    @GetMapping
    public ResponseEntity<List<MaintenanceResponseDTO>> getAllMaintenances() {
        List<MaintenanceResponseDTO> maintenances = maintenanceService.findAllMaintenances();
        return ResponseEntity.ok(maintenances);
    }

    // Obtener los mantenimientos por tipo
    @GetMapping("/type/{type}")
    public ResponseEntity<List<MaintenanceResponseDTO>> getMaintenancesBYType(@PathVariable String type){
        List<MaintenanceResponseDTO> maintenances = maintenanceService.findMaintenancesByType(type);
        return ResponseEntity.ok(maintenances);
    }

    // Buscar Mantenimientos por rango de fechas
    @GetMapping("/by-date-range")
    public ResponseEntity<List<MaintenanceResponseDTO>> getMaintenancesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){

        List<MaintenanceResponseDTO> maintenances = maintenanceService.findMaintenancesByDateRange(startDate, endDate);
        return ResponseEntity.ok(maintenances);
    }


    // Eliminar un mantenimiento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenance(@PathVariable Integer id) {
        maintenanceService.deleteMaintenanceById(id);
        return ResponseEntity.noContent().build();
    }
}
