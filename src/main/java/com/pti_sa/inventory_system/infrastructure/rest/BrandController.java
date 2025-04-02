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
@RequestMapping("/api/v1/admin/brands")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    // Crear una nueva marca
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

    // Obtener todas las Marcas
    @GetMapping
    public ResponseEntity<List<BrandResponseDTO>> getAllStatuses(){
        List<BrandResponseDTO> brands = brandService.findAllBrands();
        return ResponseEntity.ok(brands);
    }

    // Actualizar una Marca
    @PutMapping("/{id}")
    public ResponseEntity<Brand> updateBrand(@PathVariable Integer id, @RequestBody Brand brand) {
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
        brand.setId(id);
        brand.setUpdatedBy(updatedBy);

        Brand updatedBrand = brandService.updateBrand(brand);
        return ResponseEntity.ok(updatedBrand);
    }


    // Obtener Marca por ID
    @GetMapping("/{id}")
    public ResponseEntity<BrandResponseDTO>getStatusById(@PathVariable Integer id){
        Optional<BrandResponseDTO> brand = brandService.findBrandById(id);
        return brand.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Obtener la cantidad de dispositivos por estado
    @GetMapping("/device-brand-count")
    public ResponseEntity<Map<String, Long>> getDeviceCountByStatus() {
        Map<String, Long> statusCount = brandService.countDevicesByBrand();
        return ResponseEntity.ok(statusCount);
    }


    // Eliminar un Status
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatus(@PathVariable Integer id) {
        brandService.deleteBrandById(id);
        return ResponseEntity.noContent().build();
    }
}
