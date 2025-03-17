package com.pti_sa.inventory_system.infrastructure.adapter;

import com.pti_sa.inventory_system.infrastructure.entity.MaintenanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IMaintenanceJpaRepository extends JpaRepository<MaintenanceEntity, Integer> {
    List<MaintenanceEntity> findByDeviceId(Integer deviceId);
    List<MaintenanceEntity> findByMaintenanceType(String maintenanceType);
    List<MaintenanceEntity> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    Optional<MaintenanceEntity> findTopByOrderByMaintenanceDateDesc();
}
