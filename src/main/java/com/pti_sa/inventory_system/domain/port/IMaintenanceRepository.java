package com.pti_sa.inventory_system.domain.port;

import com.pti_sa.inventory_system.domain.model.Logbook;
import com.pti_sa.inventory_system.domain.model.Maintenance;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IMaintenanceRepository {
    Maintenance save(Maintenance maintenance);
    Maintenance update(Maintenance maintenance);
    Optional<Maintenance> findById(Integer id);
    void deleteById(Integer id);
    List<Maintenance> findAll();
    List<Maintenance> findByDeviceId(Integer deviceId);
    List<Maintenance> findByType(String maintenanceType);
    List<Maintenance> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

}
