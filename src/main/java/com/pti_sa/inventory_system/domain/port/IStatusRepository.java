package com.pti_sa.inventory_system.domain.port;

import com.pti_sa.inventory_system.domain.model.Status;

import java.util.List;
import java.util.Optional;

public interface IStatusRepository {
    Status save(Status status);
    Optional<Status>findById(Long id);
    List<Status>findAll();
    void deleteById(Long id);
}
