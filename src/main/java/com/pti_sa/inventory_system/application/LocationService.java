package com.pti_sa.inventory_system.application;

import com.pti_sa.inventory_system.domain.model.Location;
import com.pti_sa.inventory_system.domain.port.ILocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    private final ILocationRepository iLocationRepository;

    public LocationService(ILocationRepository iLocationRepository) {
        this.iLocationRepository = iLocationRepository;
    }

    // Guardar una ubicación
    public Location saveLocation(Location location){
        location.createAudit(location.getCreatedBy());
        return iLocationRepository.save(location);
    }

    // Actualizar ubicación
    public Location updateLocation(Location location){
        location.createAudit(location.getUpdatedBy());
        return iLocationRepository.update(location);
    }

    // Buscar ubicación por ID
    public Optional<Location> findLocationById(Integer id){
        return iLocationRepository.findById(id);
    }

    // Buscar una ubicación por su nombre
    public Optional<Location> findLocationByName(String name){
        return iLocationRepository.findByName(name);
    }

    // Obtener todas las ubicaciones
    public List<Location> findAllLocations(){
        return iLocationRepository.findAll();
    }

    // Eliminar una ubicación por ID
    public void deleteLocationById(Integer id){
        iLocationRepository.deleteById(id);
    }
}
