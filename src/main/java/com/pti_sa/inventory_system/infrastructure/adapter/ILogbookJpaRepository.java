package com.pti_sa.inventory_system.infrastructure.adapter;

import com.pti_sa.inventory_system.infrastructure.entity.LogbookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ILogbookJpaRepository extends JpaRepository<LogbookEntity, Integer> {
    List<LogbookEntity> findByDeviceId(Integer deviceId);
    List<LogbookEntity> findByUserId(Integer userId);
    List<LogbookEntity> findByStatusName(String statusName);
    List<LogbookEntity> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<LogbookEntity> findAllByDeletedFalse();
}
