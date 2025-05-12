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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Tag(name = "Marcas", description = "Operaciones relacionados con marcas")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/brands")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @Operation(summary = "Registrar una nueva marca", description = "Crea una nueva marca en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marca creada correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "400", description = "Error en la validación")
    })
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

    @Operation(summary = "Listar todas las marcas", description = "Devuelve todas las marcas registradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de marcas obtenida"),
            @ApiResponse(responseCode = "204", description = "Sin contenido")
    })
    // Obtener todas las Marcas
    @GetMapping
    public ResponseEntity<List<BrandResponseDTO>> getAllBrands(){
        List<BrandResponseDTO> brands = brandService.findAllBrands();
        return ResponseEntity.ok(brands);
    }

    @Operation(summary = "Actualizar una marca", description = "Actualiza los datos de una marca existente.")
    @ApiResponses(value  = {
            @ApiResponse(responseCode = "200", description = "Marca actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Marca no encontrada")
    })
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

    @Operation(summary = "Obtener una marca por ID", description = "Devuelve los detalles de una marca específica por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marca encontrada"),
            @ApiResponse(responseCode = "404", description = "Marca no encontrada")
    })

    // Obtener Marca por ID
    @GetMapping("/{id}")
    public ResponseEntity<BrandResponseDTO>getBrandById(@PathVariable Integer id){
        Optional<BrandResponseDTO> brand = brandService.findBrandById(id);
        return brand.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener cantidad de dispositivos por marca", description = "Devuelve el número de dispositivos agrupados por marca.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conteo obtenido correctamente")
    })

    // Obtener la cantidad de dispositivos por Marca
    @GetMapping("/device-brand-count")
    public ResponseEntity<Map<String, Long>> getDeviceCountByBrand() {
        Map<String, Long> statusCount = brandService.countDevicesByBrand();
        return ResponseEntity.ok(statusCount);
    }

    @Operation(summary = "Eliminar una marca", description = "Elimina una marca por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Marca eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Marca no encontrada")
    })
    // Eliminar Marca
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Integer id) {
        brandService.deleteBrandById(id);
        return ResponseEntity.noContent().build();
    }
}
