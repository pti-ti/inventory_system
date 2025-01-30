package com.pti_sa.inventory_system.domain.port;

import com.pti_sa.inventory_system.domain.model.Maintenance;

import java.util.List;
import java.util.Optional;

public interface IMaintenanceRepository {
    Maintenance save(Maintenance maintenance);
    Optional<Maintenance> findById(Long id);
    List<Maintenance> findAll();
    void deleteById(Long id);
}
