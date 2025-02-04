package com.pti_sa.inventory_system.infrastructure.adapter;

import com.pti_sa.inventory_system.infrastructure.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ILocationJpaRepository extends JpaRepository<LocationEntity, Integer> {
    Optional<LocationEntity> findByName(String name);
    boolean existsByName(String name);
}
