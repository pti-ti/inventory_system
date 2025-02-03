package com.pti_sa.inventory_system.infrastructure.adapter;

import com.pti_sa.inventory_system.infrastructure.entity.MaintenanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IMaintenanceJpaRepository extends JpaRepository<MaintenanceEntity, Integer> {
    List<MaintenanceEntity> findByDeviceId(Integer deviceId);
}
