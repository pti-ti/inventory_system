package com.pti_sa.inventory_system.infrastructure.adapter;

import com.pti_sa.inventory_system.infrastructure.entity.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IStatusJpaRepository extends JpaRepository<StatusEntity, Integer> {
    Optional<StatusEntity> findByName(String name);
    boolean existsByName(String name);
    @Query("SELECT s.name, COUNT(d) FROM DeviceEntity d JOIN d.status s WHERE d.deleted = false GROUP BY s.name")
    List<Object[]> countDevicesByStatus();



}
