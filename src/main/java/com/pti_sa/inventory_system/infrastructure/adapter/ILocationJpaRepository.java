package com.pti_sa.inventory_system.infrastructure.adapter;

import com.pti_sa.inventory_system.infrastructure.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ILocationJpaRepository extends JpaRepository<LocationEntity, Integer> {
    Optional<LocationEntity> findByName(String name);
    boolean existsByName(String name);

    @Query("SELECT l.name, COUNT(d.id) " +
            "FROM DeviceEntity d " +
            "JOIN d.location l " +
            "WHERE d.deleted = false " +
            "GROUP BY l.name " +
            "ORDER BY l.name")
    List<Object[]> countDevicesByLocation();

    @Query("SELECT l.name, d.type, COUNT(DISTINCT d.id) " +
            "FROM DeviceEntity d " +
            "JOIN d.location l " +
            "WHERE d.deleted = false " +
            "GROUP BY l.name, d.type " +
            "ORDER BY l.name, d.type")
    List<Object[]> countDevicesByLocationAndType();

}
