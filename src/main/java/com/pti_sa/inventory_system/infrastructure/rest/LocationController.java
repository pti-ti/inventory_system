package com.pti_sa.inventory_system.infrastructure.rest;

import com.pti_sa.inventory_system.application.LocationService;
import com.pti_sa.inventory_system.application.dto.response.LocationResponseDTO;
import com.pti_sa.inventory_system.domain.model.Location;
import com.pti_sa.inventory_system.infrastructure.service.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/locations")
@Tag(name = "Ubicaciones", description = "Operaciones relacionadas con la gestión de ubicaciones")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @Operation(summary = "Crear una nueva ubicación", description = "Permite crear una nueva ubicación en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ubicación creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "401", description =  "No autenticado")
    })
    @PostMapping("/create")
    public ResponseEntity<LocationResponseDTO> createLocation(@RequestBody Location location) {
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

        LocationResponseDTO savedLocation = locationService.saveLocation(location, createdBy);
        return ResponseEntity.ok(savedLocation);
    }

    @Operation(summary = "Actualizar una ubicación", description = "Actualiza los datosde una ubicación por ID")
    // Actualizar ubicación
    @PutMapping("/{id}")
    public ResponseEntity<LocationResponseDTO> updateLocation(@PathVariable Integer id, @RequestBody Location location) {
        location.setId(id);
        return ResponseEntity.ok(locationService.updateLocation(location));
    }

    @Operation(summary = "Obtener ubicación por ID", description = "Recupera una ubicación especifica usando su ID")
    // Obtener ubicación por id
    @GetMapping("/{id}")
    public ResponseEntity<LocationResponseDTO> getLocationById(@PathVariable Integer id) {
        Optional<LocationResponseDTO> location = locationService.findLocationById(id);
        return location.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar todas las ubicaciones", description = "Recupera todas las ubicaciones registradas en el sistema")

    // Obtener todas las ubicaciones
    @GetMapping
    public ResponseEntity<List<LocationResponseDTO>> getAllLocations() {
        return ResponseEntity.ok(locationService.findAllLocations());
    }

    @Operation(summary = "Cantidad de dispositivos por ubicación", description = "Devuelve un conteo de dispositivos por ubicación")
    // Obtener la cantidad de dispositivos por ubicación
    @GetMapping("device-location-count")
    public ResponseEntity<Map<String, Long>> getDeviceCountByLocation(){
        Map<String, Long> locationCount = locationService.countDevicesByLocation();
        return ResponseEntity.ok(locationCount);
    }

    @Operation(summary = "Cantidad de dispositivos por ubicación y tipo", description = "Devuelve un conteo de dispositivos por ubicación y tipo de dispositivos")
    // Obtener la cantidad de dispositivos por ubicación con el tipo
    @GetMapping("device-location-type-count")
    public ResponseEntity<Map<String, Map<String, Long>>> getDeviceCountByLocationAndType(){
        Map<String, Map<String, Long>> locationTypeCount = locationService.countDevicesByLocationAndType();
        return ResponseEntity.ok(locationTypeCount);
    }

    @Operation(summary = "Eliminar ubicación", description = "Elimina una ubicación especifica por su ID")
    // Eliminar una ubicación
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Integer id) {
        locationService.deleteLocationById(id);
        return ResponseEntity.noContent().build();
    }
}
