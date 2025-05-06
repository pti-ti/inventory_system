package com.pti_sa.inventory_system.infrastructure.rest;

import com.pti_sa.inventory_system.application.LogbookService;
import com.pti_sa.inventory_system.application.dto.request.LogbookRequestDTO;
import com.pti_sa.inventory_system.application.dto.response.DeviceResponseDTO;
import com.pti_sa.inventory_system.application.dto.response.LogbookResponseDTO;
import com.pti_sa.inventory_system.domain.model.Logbook;
import com.pti_sa.inventory_system.infrastructure.mapper.LogbookMapper;
import com.pti_sa.inventory_system.infrastructure.service.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@Tag(name = "Bitácoras", description = "Operaciones relacionadas con el registro y gestión de las bitácoras")
@RequestMapping("/api/v1/logbooks")
public class LogbookController {

    private static final Logger logger = LoggerFactory.getLogger(LogbookController.class);

    private final LogbookService logbookService;

    public LogbookController(LogbookService logbookService, LogbookMapper logbookMapper) {
        this.logbookService = logbookService;
    }

    @Operation(summary = "Registrar una bitácora", description = "Permite registrar una nueva bitácora en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bitácora registrada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o usuario no autenticado"),
            @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @PostMapping("/register")
    public ResponseEntity<LogbookResponseDTO> createLogbook(@RequestBody Logbook logbook) {
        log.info("📌 Datos recibidos en la API: {}", logbook);
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

    @Operation(summary = "Actualizar una bitácora", description = "Actualiza una bitácora existente por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bitácora actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "No se pudo obtener el usuario autenticado"),
            @ApiResponse(responseCode = "401", description = "No autenticado")
    })
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

    @Operation(summary = "Listar todas las bitácoras", description = "Obtiene todas las bitácoras registradas (sin eliminadas)")
    @ApiResponse(responseCode = "200", description = "Lista de bitácoras obtenida correctamente")

    @GetMapping
    public ResponseEntity<List<LogbookResponseDTO>> getAllLogbooks() {
        List<LogbookResponseDTO> logbooks = logbookService.findAllByDeletedFalse();
        return ResponseEntity.ok(logbooks);
    }

    @Operation(summary = "Obtener bitácoras por estado", description = "Filtra bitácoras por su estado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bitácoras filtradas correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron bitácoras con ese estado")
    })
    @GetMapping("/status/{statusName}")
    public ResponseEntity<List<LogbookResponseDTO>> getLogbooksByStatus(@PathVariable("statusName") String statusName){
        List<LogbookResponseDTO> logbooks = logbookService.findLogbooksByStatus(statusName);
        return ResponseEntity.ok(logbooks);
    }

    @Operation(summary = "Buscar bitácoras por rango de fechas", description = "Recupera bitácoras entre dos fechas específicas")
    @ApiResponse(responseCode = "200", description = "Bitácoras encontradas dentro del rango de fechas")
    @GetMapping("/by-date-range")
    public ResponseEntity<List<LogbookResponseDTO>> getLogbooksByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<LogbookResponseDTO> logbooks = logbookService.findLogbooksByDateRange(startDate, endDate);
        return ResponseEntity.ok(logbooks);
    }

    @Operation(summary = "Eliminar una bitácora", description = "Elimina (lógicamente) una bitácora por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bitácora eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Bitácora no encontrada")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLogbook(@PathVariable Integer id) {
        logbookService.deleteLogbookById(id);
        return ResponseEntity.ok("Bitácora con ID " + id + " eliminado exitosamente.");
    }

}
