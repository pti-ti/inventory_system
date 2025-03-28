package com.pti_sa.inventory_system.infrastructure.adapter;

import com.pti_sa.inventory_system.infrastructure.entity.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IBrandJpaRepository extends JpaRepository<BrandEntity, Integer> {

    Optional<BrandEntity> findByName(String name);
    boolean existsByName(String name);

    // Query
    //List<Object[]> countDevicesByBrand();
}
