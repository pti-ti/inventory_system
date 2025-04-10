package com.pti_sa.inventory_system.infrastructure.adapter;

import com.pti_sa.inventory_system.infrastructure.entity.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IDeviceJpaRepository extends JpaRepository<DeviceEntity, Integer> {
    Optional<DeviceEntity> findByCode(String code);
    Optional<DeviceEntity> findBySerial(String serial);
    List<DeviceEntity> findByDeletedFalse();
    List<DeviceEntity> findByStatusId(Integer statusId);
    boolean existsBySerial(String serial);

    @Query("SELECT d.type, COUNT(d) FROM DeviceEntity d GROUP BY d.type")
    Map<String, Long> countDevicesByType();
    @Query("SELECT SUM(d.price) FROM DeviceEntity d WHERE d.deleted = false")
    BigDecimal getTotalInventoryValue();
}
