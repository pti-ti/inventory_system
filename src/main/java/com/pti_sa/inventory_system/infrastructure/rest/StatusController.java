package com.pti_sa.inventory_system.infrastructure.rest;

import com.pti_sa.inventory_system.application.StatusService;
import com.pti_sa.inventory_system.domain.model.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/status")
public class StatusController {

    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    // Crear un nuevo Status
    @PostMapping
    public ResponseEntity<Status> createStatus(@RequestBody Status status){
        Status createdStatus = statusService.saveStatus(status);
        return ResponseEntity.ok(createdStatus);
    }

    // Actualizar un Status
    @PutMapping("/{id}")
    public ResponseEntity<Status> updateStatus(@PathVariable Integer id, @RequestBody Status status){
        status.setId(id);
        Status updatedStatus = statusService.updateStatus(status);
        return ResponseEntity.ok(updatedStatus);
    }

    // Obtener Status por ID
    @GetMapping("{id}")
    public ResponseEntity<Status> getStatusById(@PathVariable Integer id){
        Optional<Status> status = statusService.findStatusById(id);
        return status.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    // Obtener todos los Status
    @GetMapping
    public ResponseEntity<List<Status>> getAllStatuses(){
        List<Status> statuses = statusService.findAllStatuses();
        return ResponseEntity.ok(statuses);
    }

    // Eliminar un Status
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatus(@PathVariable Integer id){
        statusService.deleteStatusById(id);
        return ResponseEntity.noContent().build();
    }
}
