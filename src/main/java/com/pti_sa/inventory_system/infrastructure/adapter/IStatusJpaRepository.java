package com.pti_sa.inventory_system.infrastructure.adapter;

import com.pti_sa.inventory_system.infrastructure.entity.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IStatusJpaRepository extends JpaRepository<StatusEntity, Integer> {
    Optional<StatusEntity> findByName(String name);
    boolean existsByName(String name);
}
