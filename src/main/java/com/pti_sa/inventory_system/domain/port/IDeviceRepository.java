package com.pti_sa.inventory_system.domain.port;

import com.pti_sa.inventory_system.domain.model.Device;

import java.util.List;
import java.util.Optional;

public interface IDeviceRepository {
    Device save(Device device);
    Device update(Device device);
    Optional<Device>findById(Integer id);
    Optional<Device>findByCode(String code);
    List<Device> findAll();
    void deleteById(Integer id);
}
