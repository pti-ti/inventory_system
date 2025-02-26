package com.pti_sa.inventory_system.infrastructure.rest;

import com.pti_sa.inventory_system.application.LogbookService;
import com.pti_sa.inventory_system.application.dto.response.DeviceResponseDTO;
import com.pti_sa.inventory_system.application.dto.response.LogbookResponseDTO;
import com.pti_sa.inventory_system.domain.model.Device;
import com.pti_sa.inventory_system.domain.model.Logbook;
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
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin/logbooks")
public class LogbookController {

    private final LogbookService logbookService;

    public LogbookController(LogbookService logbookService) {
        this.logbookService = logbookService;
    }

    // Crear Bitácora
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @PostMapping("/register")
    public ResponseEntity<?> createLogbook(@RequestBody Logbook logbook) {

        System.out.println("Logbook recibido: " + logbook);


        if (logbook.getDevice() == null || logbook.getDevice().getId() == null) {
            System.out.println("Error: El dispositivo es requerido.");

            return ResponseEntity.badRequest().body("El dispositivo es requerido.");
        }

        // Obtener usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("Error: Usuario no autenticado.");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado.");
        }

        Object principal = authentication.getPrincipal();
        Integer createdBy = null;

        if (principal instanceof CustomUserDetails) {
            createdBy = ((CustomUserDetails) principal).getId();
        }

        if (createdBy == null) {
            System.out.println("Error: No se pudo obtener el usuario creador.");

            return ResponseEntity.badRequest().body("No se pudo obtener el usuario creador.");
        }

        System.out.println("Usuario autenticado ID: " + createdBy);


        // Crear auditoría
        logbook.createAudit(createdBy);

        try {
            LogbookResponseDTO savedLogbook = logbookService.saveLogbook(logbook);
            System.out.println("Bitácora registrada con éxito: " + savedLogbook);

            return ResponseEntity.ok(savedLogbook);
        } catch (IllegalArgumentException e) {
            System.out.println("Error al guardar la bitácora: " + e.getMessage());

            return ResponseEntity.badRequest().body(e.getMessage()); // Manejo de error si el dispositivo no existe
        }
    }



    // Crear logbook
//    @PostMapping
//    public ResponseEntity<LogbookResponseDTO> createLogbook(@RequestBody Logbook logbook) {
//        LogbookResponseDTO createdLogbook = logbookService.saveLogbook(logbook);
//        return ResponseEntity.ok(createdLogbook);
//    }
    /*@PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @PostMapping("/register")
    public ResponseEntity<LogbookResponseDTO> createLogbook(@RequestBody Logbook logbook) {
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
            return ResponseEntity.badRequest().body(null);
        }

        logbook.createAudit(createdBy);
        return ResponseEntity.ok(logbookService.saveLogbook(logbook));
    }*/


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
