package com.pti_sa.inventory_system.infrastructure.rest;

import com.pti_sa.inventory_system.application.LogbookService;
import com.pti_sa.inventory_system.application.dto.response.LogbookResponseDTO;
import com.pti_sa.inventory_system.domain.model.Logbook;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/logbooks")
public class LogbookController {

    private final LogbookService logbookService;

    public LogbookController(LogbookService logbookService) {
        this.logbookService = logbookService;
    }

    // Crear logbook
    @PostMapping
    public ResponseEntity<LogbookResponseDTO> createLogbook(@RequestBody Logbook logbook) {
        LogbookResponseDTO createdLogbook = logbookService.saveLogbook(logbook);
        return ResponseEntity.ok(createdLogbook);
    }

    // Actualizar logbook
    @PutMapping("/{id}")
    public ResponseEntity<LogbookResponseDTO> updateLogbook(@PathVariable Integer id, @RequestBody Logbook logbook) {
        logbook.setId(id);
        LogbookResponseDTO updatedLogbook = logbookService.updateLogbook(logbook);
        return ResponseEntity.ok(updatedLogbook);
    }

    // Obtener todas las bitácoras
    @GetMapping
    public ResponseEntity<List<LogbookResponseDTO>> getAllLogbooks() {
        List<LogbookResponseDTO> logbooks = logbookService.findAllLogbooks();
        return ResponseEntity.ok(logbooks);
    }

    // Obtener las bitácoras por el status
    @GetMapping("/status/{statusName}")
    public ResponseEntity<List<LogbookResponseDTO>> getLogbooksByStatus(@PathVariable("statusName") String statusName){
        List<LogbookResponseDTO> logbooks = logbookService.findLogbooksByStatus(statusName);
        return ResponseEntity.ok(logbooks);
    }

    // Buscar registros por rango de fechas
    @GetMapping("/by-date-range")
    public ResponseEntity<List<LogbookResponseDTO>> getLogbooksByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<LogbookResponseDTO> logbooks = logbookService.findLogbooksByDateRange(startDate, endDate);
        return ResponseEntity.ok(logbooks);
    }

    // Eliminar un logbook
    @DeleteMapping("/{id}")
    public ResponseEntity<LogbookResponseDTO> getLogbookById(@PathVariable Integer id) {
        Optional<LogbookResponseDTO> logbook = logbookService.findLogbookById(id);
        return logbook.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
