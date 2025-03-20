package com.pti_sa.inventory_system.domain.port;

import com.pti_sa.inventory_system.domain.model.Status;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IStatusRepository {
    Status save(Status status);
    Status update(Status status);
    Optional<Status>findById(Integer id);
    void deleteById(Integer id);
    List<Status>findAll();
    Map<String, Long> countDevicesByStatus();
}
