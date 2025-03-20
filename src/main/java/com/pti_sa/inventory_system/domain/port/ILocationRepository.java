package com.pti_sa.inventory_system.domain.port;

import com.pti_sa.inventory_system.domain.model.Location;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ILocationRepository {
    Location save(Location location);
    Location update(Location location);
    Optional<Location>findById(Integer id);
    Optional<Location>findByName(String name);
    void deleteById(Integer id);
    boolean existsByName(String name);
    List<Location>findAll();
    Map<String, Long> countDevicesByLocation();
    Map<String, Map<String, Long>> countDevicesByLocationAndType();

}

