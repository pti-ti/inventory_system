package com.pti_sa.inventory_system.application;

import com.pti_sa.inventory_system.application.dto.LocationResponseDTO;
import com.pti_sa.inventory_system.domain.model.Location;
import com.pti_sa.inventory_system.domain.port.ILocationRepository;
import com.pti_sa.inventory_system.infrastructure.mapper.LocationMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private final ILocationRepository iLocationRepository;
    private final LocationMapper locationMapper;

    public LocationService(ILocationRepository iLocationRepository, LocationMapper locationMapper) {
        this.iLocationRepository = iLocationRepository;
        this.locationMapper = locationMapper;
    }

    // Guardar una ubicación
    public LocationResponseDTO saveLocation(Location location){
        location.createAudit(location.getCreatedBy());
        Location savedLocation = iLocationRepository.save(location);
        return locationMapper.toDTO(savedLocation);
    }

    // Actualizar ubicación
    public LocationResponseDTO updateLocation(Location location){
        location.createAudit(location.getUpdatedBy());
        Location updatedLocation = iLocationRepository.update(location);
        return locationMapper.toDTO(updatedLocation);
    }

    // Buscar ubicación por ID
    public Optional<LocationResponseDTO> findLocationById(Integer id){
        return iLocationRepository.findById(id).map(locationMapper::toDTO);
    }

    // Buscar una ubicación por su nombre
    public Optional<LocationResponseDTO> findLocationByName(String name){
        return iLocationRepository.findByName(name).map(locationMapper::toDTO);
    }

    // Obtener todas las ubicaciones
    public List<LocationResponseDTO> findAllLocations(){
        return iLocationRepository.findAll()
                .stream()
                .map(locationMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Eliminar una ubicación por ID
    public void deleteLocationById(Integer id){
        iLocationRepository.deleteById(id);
    }
}
