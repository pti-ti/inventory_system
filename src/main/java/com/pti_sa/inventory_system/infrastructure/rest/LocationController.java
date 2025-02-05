package com.pti_sa.inventory_system.infrastructure.rest;

import com.pti_sa.inventory_system.application.LocationService;
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
    public ResponseEntity<Location> createLocation(@RequestBody Location location){
        Location createdLocation = locationService.saveLocation(location);
        return ResponseEntity.ok(createdLocation);
    }

    // Actualizar ubicación
    @PutMapping("/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable Integer id, @RequestBody Location location){
        location.setId(id);
        Location updatedLocation = locationService.updateLocation(location);
        return ResponseEntity.ok(updatedLocation);
    }

    // Obtener ubicación por id
    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable Integer id){
        Optional<Location> location = locationService.findLocationById(id);
        return location.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Obtener todas las ubicacion
    @GetMapping
    public ResponseEntity<List<Location>> getAllLocations(){
        List<Location> locations = locationService.findAllLocations();
        return ResponseEntity.ok(locations);
    }

    //Eliminar una ubicación
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Integer id){
        locationService.deleteLocationById(id);
        return ResponseEntity.noContent().build();
    }
}
