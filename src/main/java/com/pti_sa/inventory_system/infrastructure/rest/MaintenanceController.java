package com.pti_sa.inventory_system.infrastructure.rest;

import com.pti_sa.inventory_system.application.MaintenanceService;
import com.pti_sa.inventory_system.domain.model.Maintenance;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/maintenances")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    public MaintenanceController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    // Crear Mantenimiento
    @PostMapping
    public ResponseEntity<Maintenance> createMaintenance(@RequestBody Maintenance maintenance){
        Maintenance createdMaintenance = maintenanceService.saveMaintenance(maintenance);
        return ResponseEntity.ok(createdMaintenance);
    }

    // Actualizar mantenimiento
    @PutMapping("/{id}")
    public ResponseEntity<Maintenance> updateMaintenance(@PathVariable Integer id, @RequestBody Maintenance maintenance){
        maintenance.setId(id);
        Maintenance updatedMaintenance = maintenanceService.updateMaintenance(maintenance);
        return ResponseEntity.ok(updatedMaintenance);
    }

    // Obtener Mantenimiento por id
    @GetMapping("{id}")
    public ResponseEntity<Maintenance> getMaintenanceById(@PathVariable Integer id){
        Optional<Maintenance> maintenance = maintenanceService.findMaintenanceById(id);
        return maintenance.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Obtener todos los mantenimientos
    @GetMapping
    public ResponseEntity<List<Maintenance>> getAllMaintenances(){
        List<Maintenance> maintenances = maintenanceService.findAllMaintenances();
        return ResponseEntity.ok(maintenances);
    }

    // Eliminar un mantenimiento
    @DeleteMapping("/{id")
    public ResponseEntity<Void> deleteMaintenance(@PathVariable Integer id){
        maintenanceService.deleteMaintenanceById(id);
        return ResponseEntity.noContent().build();
    }
}
