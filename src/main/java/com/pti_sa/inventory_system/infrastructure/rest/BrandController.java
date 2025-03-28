package com.pti_sa.inventory_system.infrastructure.rest;

import com.pti_sa.inventory_system.application.BrandService;
import com.pti_sa.inventory_system.application.dto.response.BrandResponseDTO;
import com.pti_sa.inventory_system.application.dto.response.StatusResponseDTO;
import com.pti_sa.inventory_system.domain.model.Brand;
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

    //Crear una nueva marca
    @PostMapping("/create")
    public ResponseEntity<BrandResponseDTO> createBrand(@RequestBody Brand brand){
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

    // Actualizar una marca
    @PutMapping("/{id}")
    public ResponseEntity<Brand> updateBrand(@PathVariable Integer id, @RequestBody Brand brand){
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

    // Obtener brand por ID
    @GetMapping
    public ResponseEntity<BrandResponseDTO>getBrandById(@PathVariable Integer id){
        Optional<BrandResponseDTO> brands = brandService.findBrandById(id);
        return brands.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Obtener todas las marcas
    @GetMapping
    public ResponseEntity<List<BrandResponseDTO>> getAllBrands(){
        List<BrandResponseDTO> brands = brandService.findAllBrands();
        return ResponseEntity.ok(brands);
    }

    // Obtener la cantidad de dispositivos por marca
    @GetMapping("/device-brand-count")
    public ResponseEntity<Map<String, Long>>getDeviceCountByBrand(){
        Map<String, Long> brandCount = brandService.countDevicesByBrand();
        return ResponseEntity.ok(brandCount);
    }

    // Eliminar un Brand
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Integer id){
        brandService.deleteBrandById(id);
        return ResponseEntity.noContent().build();
    }
}

