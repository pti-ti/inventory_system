package com.pti_sa.inventory_system.domain.port;

import com.pti_sa.inventory_system.domain.model.Location;

import java.util.List;
import java.util.Optional;

public interface ILocationRepository {
    Location save(Location location);
    Optional<Location>findById(Long id);
    List<Location> findAll();
    void deleteById(Long id);
}
