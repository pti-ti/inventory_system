package com.pti_sa.inventory_system.infrastructure.rest;

import com.pti_sa.inventory_system.application.LogbookService;
import com.pti_sa.inventory_system.application.dto.request.LogbookRequestDTO;
import com.pti_sa.inventory_system.application.dto.response.DeviceResponseDTO;
import com.pti_sa.inventory_system.application.dto.response.LogbookResponseDTO;
import com.pti_sa.inventory_system.domain.model.Logbook;
import com.pti_sa.inventory_system.infrastructure.mapper.LogbookMapper;
import com.pti_sa.inventory_system.infrastructure.service.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/api/v1/admin/logbooks")
public class LogbookController {

    private static final Logger logger = LoggerFactory.getLogger(LogbookController.class);

    private final LogbookService logbookService;

    public LogbookController(LogbookService logbookService, LogbookMapper logbookMapper) {
        this.logbookService = logbookService;
    }

    // Crear Bitácora
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
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
        LogbookResponseDTO savedLogbook = logbookService.saveLogbook(logbook);
        return ResponseEntity.ok(savedLogbook);
    }

    // Actualizar logbook
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLogbook(@PathVariable Integer id, @RequestBody Logbook logbook) {
        logbook.setId(id);


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()) {
            System.out.println("Error: Usuario no autenticado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado.");
        }

        Object principal = authentication.getPrincipal();
        Integer updatedBy = null;

        if(principal instanceof CustomUserDetails){
            updatedBy = ((CustomUserDetails) principal).getId();
        }

        if(updatedBy == null){
            System.out.println("Error: No se pudo obtener el usuario autenticado.");
            return ResponseEntity.badRequest().body("No se pudo obtener el usuario autenticado.");
        }

        System.out.println(" Usuario autenticado para updateBy: " + updatedBy);

        logbook.setUpdatedBy(updatedBy);

        LogbookResponseDTO updatedLogbook = logbookService.updateLogbook(logbook);
        return ResponseEntity.ok((updatedLogbook));
    }



    // Obtener todas las bitácoras
    @GetMapping
    public ResponseEntity<List<LogbookResponseDTO>> getAllLogbooks() {
        List<LogbookResponseDTO> logbooks = logbookService.findAllByDeletedFalse();
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
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLogbook(@PathVariable Integer id) {
        logbookService.deleteLogbookById(id);
        return ResponseEntity.ok("Bitácora con ID " + id + " eliminado exitosamente.");
    }

}
