package com.pti_sa.inventory_system.infrastructure.rest;

import com.pti_sa.inventory_system.application.LocationService;
import com.pti_sa.inventory_system.application.dto.LocationResponseDTO;
import com.pti_sa.inventory_system.domain.model.Location;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    // Crear ubicación
    @PostMapping
    public ResponseEntity<LocationResponseDTO> createLocation(@RequestBody Location location) {
        return ResponseEntity.ok(locationService.saveLocation(location));
    }

    // Actualizar ubicación
    @PutMapping("/{id}")
    public ResponseEntity<LocationResponseDTO> updateLocation(@PathVariable Integer id, @RequestBody Location location) {
        location.setId(id);
        return ResponseEntity.ok(locationService.updateLocation(location));
    }

    // Obtener ubicación por id
    @GetMapping("/{id}")
    public ResponseEntity<LocationResponseDTO> getLocationById(@PathVariable Integer id) {
        Optional<LocationResponseDTO> location = locationService.findLocationById(id);
        return location.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Obtener todas las ubicaciones
    @GetMapping
    public ResponseEntity<List<LocationResponseDTO>> getAllLocations() {
        return ResponseEntity.ok(locationService.findAllLocations());
    }

    // Eliminar una ubicación
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Integer id) {
        locationService.deleteLocationById(id);
        return ResponseEntity.noContent().build();
    }
}
