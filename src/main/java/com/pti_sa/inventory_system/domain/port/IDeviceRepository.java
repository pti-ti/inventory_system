package com.pti_sa.inventory_system.domain.port;

import com.pti_sa.inventory_system.domain.model.Device;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IDeviceRepository {
    Device save(Device device);
    Device update(Device device);
    Optional<Device>findById(Integer id);
    Optional<Device>findByCode(String code);
    Optional<Device>findBySerial(String serial);
    List<Device>findByStatusId(Integer statusId);
    List<Device> findAll();
    List<Device> findAllByDeletedFalse();
    List<Device> findAllById(List<Integer> ids);
    Map<String, Long> getDeviceCountsByType();
    void deleteById(Integer id);
}
