package com.pti_sa.inventory_system.domain.port;

import com.pti_sa.inventory_system.domain.model.Logbook;

import java.util.List;
import java.util.Optional;

public interface ILogbookRepository {
    Logbook save(Logbook logbook);
    Optional<Logbook> findById(Long id);
    List<Logbook> findAll();
    void deleteById(Long id);
}
