package com.pti_sa.inventory_system.infrastructure.rest;

import com.pti_sa.inventory_system.application.LogbookService;
import com.pti_sa.inventory_system.domain.model.Logbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/logbooks")
public class LogbookController {

    private final LogbookService logbookService;

    public LogbookController(LogbookService logbookService) {
        this.logbookService = logbookService;
    }

    // Crear logbook
    @PostMapping
    public ResponseEntity<Logbook> createLogbook(@RequestBody Logbook logbook){
        Logbook createdLogbook = logbookService.saveLogbook(logbook);
        return ResponseEntity.ok(createdLogbook);
    }

    // Actualizar logbook
    @PutMapping("/{id}")
    public ResponseEntity<Logbook> updateLogbook(@PathVariable Integer id, @RequestBody Logbook logbook){
        logbook.setId(id);
        Logbook updatedLogbook = logbookService.updatedLogbook(logbook);
        return ResponseEntity.ok(updatedLogbook);
    }

    // Obtener todos los logbooks
    @GetMapping
    public ResponseEntity<List<Logbook>> getAllLogbooks(){
        List<Logbook> logbooks = logbookService.findAllLogbooks();
        return ResponseEntity.ok(logbooks);
    }

    // Eliminar un logbook
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLogbook(@PathVariable Integer id){
        logbookService.deleteLogbookById(id);
        return ResponseEntity.noContent().build();
    }
}
