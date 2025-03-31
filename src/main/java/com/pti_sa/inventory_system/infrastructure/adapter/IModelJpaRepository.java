package com.pti_sa.inventory_system.infrastructure.adapter;

import com.pti_sa.inventory_system.infrastructure.entity.ModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IModelJpaRepository extends JpaRepository<ModelEntity, Integer> {
    Optional<ModelEntity> findByName(String name);
    boolean  existsByName(String name);

}
