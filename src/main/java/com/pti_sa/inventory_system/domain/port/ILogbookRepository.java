package com.pti_sa.inventory_system.domain.port;

import com.pti_sa.inventory_system.domain.model.Logbook;

import java.util.List;
import java.util.Optional;

public interface ILogbookRepository {
    Logbook save(Logbook logbook);
    Logbook update(Logbook logbook);
    Optional<Logbook> findById(Integer id);
    void deleteById(Integer id);
    List<Logbook> findAll();
    List<Logbook> findByDeviceId(Integer deviceId);
    List<Logbook> findByUserId(Integer userId);
    List<Logbook> findByLocationId(Integer locationId);
    List<Logbook>findByStatusName(String statusName);
}
