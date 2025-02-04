package com.pti_sa.inventory_system.infrastructure.adapter;

import com.pti_sa.inventory_system.infrastructure.entity.LogbookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ILogbookJpaRepository extends JpaRepository<LogbookEntity, Integer> {
    List<LogbookEntity> findByDeviceId(Integer deviceId);
    List<LogbookEntity> findByUserId(Integer userId);
}
