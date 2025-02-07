package com.pti_sa.inventory_system.infrastructure.rest;

import com.pti_sa.inventory_system.application.MaintenanceService;
import com.pti_sa.inventory_system.application.dto.MaintenanceResponseDTO;
import com.pti_sa.inventory_system.domain.model.Maintenance;
import com.pti_sa.inventory_system.infrastructure.mapper.MaintenanceMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/maintenances")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    public MaintenanceController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    // Crear Mantenimiento
    @PostMapping
    public ResponseEntity<MaintenanceResponseDTO> createMaintenance(@RequestBody Maintenance maintenance) {
        MaintenanceResponseDTO createdMaintenance = maintenanceService.saveMaintenance(maintenance);
        return ResponseEntity.ok(createdMaintenance);
    }

        // Actualizar mantenimiento
    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceResponseDTO> updateMaintenance(@PathVariable Integer id, @RequestBody Maintenance maintenance) {
        maintenance.setId(id);
        MaintenanceResponseDTO updatedMaintenance = maintenanceService.updateMaintenance(maintenance);
        return ResponseEntity.ok(updatedMaintenance);
    }

    // Obtener Mantenimiento por ID
    @GetMapping("/device/{deviceId}")
    public ResponseEntity<List<MaintenanceResponseDTO>> getMaintenancesByDeviceId(@PathVariable Integer deviceId) {
        List<MaintenanceResponseDTO> maintenances = maintenanceService.findMaintenancesByDeviceId(deviceId);
        return ResponseEntity.ok(maintenances);
    }

    // Obtener todos los mantenimientos
    @GetMapping
    public ResponseEntity<List<MaintenanceResponseDTO>> getAllMaintenances() {
        List<MaintenanceResponseDTO> maintenances = maintenanceService.findAllMaintenances();
        return ResponseEntity.ok(maintenances);
    }

    // Eliminar un mantenimiento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenance(@PathVariable Integer id) {
        maintenanceService.deleteMaintenanceById(id);
        return ResponseEntity.noContent().build();
    }
}
