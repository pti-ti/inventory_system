package com.pti_sa.inventory_system.infrastructure.rest;

import com.pti_sa.inventory_system.application.LocationService;
import com.pti_sa.inventory_system.application.dto.response.LocationResponseDTO;
import com.pti_sa.inventory_system.domain.model.Location;
import com.pti_sa.inventory_system.infrastructure.service.CustomUserDetails;
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
@RequestMapping("/api/v1/admin/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    // Crear ubicación
    //@PreAuthorize("hasAnyRole('ADMIN' , 'TECHNICIAN')")
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

    // Crear el usuario sin autenticación
    /*public ResponseEntity<LocationResponseDTO> createLocation(@RequestBody Location location) {
        // Simulación de un usuario autenticado con id 1
        Integer createdBy = 1;
        LocationResponseDTO savedLocation = locationService.saveLocation(location, createdBy);
        return ResponseEntity.ok(savedLocation);
    }*/

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

    // Obtener la cantidad de dispositivos por ubicación
    @GetMapping("device-location-count")
    public ResponseEntity<Map<String, Long>> getDeviceCountByLocation(){
        Map<String, Long> locationCount = locationService.countDevicesByLocation();
        return ResponseEntity.ok(locationCount);
    }

    // Obtener la cantidad de dispositivos por ubicación con el tipo
    @GetMapping("device-location-type-count")
    public ResponseEntity<Map<String, Map<String, Long>>> getDeviceCountByLocationAndType(){
        Map<String, Map<String, Long>> locationTypeCount = locationService.countDevicesByLocationAndType();
        return ResponseEntity.ok(locationTypeCount);
    }

    // Eliminar una ubicación
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Integer id) {
        locationService.deleteLocationById(id);
        return ResponseEntity.noContent().build();
    }
}
