package com.pti_sa.inventory_system.infrastructure.rest;

import com.pti_sa.inventory_system.application.BrandService;
import com.pti_sa.inventory_system.application.StatusService;
import com.pti_sa.inventory_system.application.dto.response.BrandResponseDTO;
import com.pti_sa.inventory_system.application.dto.response.LocationResponseDTO;
import com.pti_sa.inventory_system.application.dto.response.StatusResponseDTO;
import com.pti_sa.inventory_system.domain.model.Brand;
import com.pti_sa.inventory_system.domain.model.Location;
import com.pti_sa.inventory_system.domain.model.Status;
import com.pti_sa.inventory_system.infrastructure.service.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin/brand")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    // Crear un nuevo Status
    @PostMapping("/create")
    public ResponseEntity<BrandResponseDTO> createBrand(@RequestBody Brand brand) {
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
            return ResponseEntity.badRequest().build();
        }

        BrandResponseDTO savedBrand = brandService.saveBrand(brand, createdBy);
        return ResponseEntity.ok(savedBrand);
    }

    /*// Actualizar un Status
    @PutMapping("/{id}")
    public ResponseEntity<Status> updateStatus(@PathVariable Integer id, @RequestBody Status status) {
        // Obtener usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Object principal = authentication.getPrincipal();
        Integer updatedBy = null;
        if (principal instanceof CustomUserDetails) {
            updatedBy = ((CustomUserDetails) principal).getId();
        }

        if (updatedBy == null) {
            return ResponseEntity.badRequest().build();
        }

        // Asignar el ID del usuario que está actualizando
        status.setId(id);
        status.setUpdatedBy(updatedBy);

        Status updatedStatus = statusService.updateStatus(status);
        return ResponseEntity.ok(updatedStatus);
    }


    // Obtener Status por ID
    @GetMapping("/{id}")
    public ResponseEntity<StatusResponseDTO>getStatusById(@PathVariable Integer id){
        Optional<StatusResponseDTO> status = statusService.findStatusById(id);
        return status.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Obtener todos los Status
    @GetMapping
    public ResponseEntity<List<StatusResponseDTO>> getAllStatuses(){
        List<StatusResponseDTO> statuses = statusService.findAllStatuses();
        return ResponseEntity.ok(statuses);
    }

    // Obtener la cantidad de dispositivos por estado
    @GetMapping("/device-status-count")
    public ResponseEntity<Map<String, Long>> getDeviceCountByStatus() {
        Map<String, Long> statusCount = statusService.countDevicesByStatus();
        return ResponseEntity.ok(statusCount);
    }


    // Eliminar un Status
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatus(@PathVariable Integer id){
        statusService.deleteStatusById(id);
        return ResponseEntity.noContent().build();
    }*/
}
