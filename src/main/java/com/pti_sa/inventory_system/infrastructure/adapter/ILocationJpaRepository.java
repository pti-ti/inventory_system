package com.pti_sa.inventory_system.infrastructure.adapter;

import com.pti_sa.inventory_system.infrastructure.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ILocationJpaRepository extends JpaRepository<LocationEntity, Integer> {
    Optional<LocationEntity> findByName(String name);
    boolean existsByName(String name);
    @Query("SELECT l.name, COUNT(DISTINCT lb.device) FROM LogbookEntity lb " +
            "JOIN lb.location l " +
            "WHERE lb.deleted = false " +
            "GROUP BY l.name")
    List<Object[]> countDevicesByLocation();

    @Query("SELECT l.name, lb.device.type, COUNT(DISTINCT lb.device) FROM LogbookEntity lb " +
            "JOIN lb.location l " +
            "JOIN lb.device d " +
            "WHERE lb.deleted = false " +
            "GROUP BY l.name, lb.device.type")
    List<Object[]> countDevicesByLocationAndType();

}
