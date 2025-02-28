package com.pti_sa.inventory_system.infrastructure.adapter;

import com.pti_sa.inventory_system.infrastructure.entity.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IDeviceJpaRepository extends JpaRepository<DeviceEntity, Integer> {
    Optional<DeviceEntity> findByCode(String code);
    Optional<DeviceEntity> findBySerial(String serial);
    List<DeviceEntity> findByDeletedFalse();
    List<DeviceEntity> findByStatusId(Integer statusId);
}
