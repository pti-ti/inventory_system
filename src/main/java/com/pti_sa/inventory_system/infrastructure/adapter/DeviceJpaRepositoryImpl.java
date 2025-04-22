package com.pti_sa.inventory_system.infrastructure.adapter;

import com.pti_sa.inventory_system.domain.model.Device;
import com.pti_sa.inventory_system.domain.port.IDeviceRepository;
import com.pti_sa.inventory_system.infrastructure.entity.DeviceEntity;
import com.pti_sa.inventory_system.infrastructure.mapper.DeviceMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class DeviceJpaRepositoryImpl implements IDeviceRepository {

    private final IDeviceJpaRepository iDeviceJpaRepository;
    private final DeviceMapper deviceMapper;

    public DeviceJpaRepositoryImpl(IDeviceJpaRepository iDeviceJpaRepository, DeviceMapper deviceMapper) {
        this.iDeviceJpaRepository = iDeviceJpaRepository;
        this.deviceMapper = deviceMapper;
    }

    @Override
    public Device save(Device device) {
        DeviceEntity entity = deviceMapper.toEntity(device);
        return deviceMapper.toModel(iDeviceJpaRepository.save(entity));
    }

    @Override
    public Device update(Device device) {
        DeviceEntity entity = deviceMapper.toEntity(device);
        return deviceMapper.toModel(iDeviceJpaRepository.save(entity));
    }

    @Override
    public Optional<Device> findById(Integer id) {
        return iDeviceJpaRepository.findById(id).map(deviceMapper::toModel);
    }

    @Override
    public Optional<Device> findByCode(String code) {
        return iDeviceJpaRepository.findByCode(code).map(deviceMapper::toModel);
    }

    @Override
    public Optional<Device> findBySerial(String serial) {
        return iDeviceJpaRepository.findBySerial(serial).map(deviceMapper::toModel);
    }

    @Override
    public List<Device> findByCodeContainingIgnoreCase(String code) {
        return iDeviceJpaRepository.findByCodeContainingIgnoreCase(code)
                .stream()
                .map(deviceMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Device> findByStatusId(Integer statusId) {
        return iDeviceJpaRepository.findByStatusId(statusId)
                .stream()
                .map(deviceMapper::toModel)
                .collect(Collectors.toList());
    }


    @Override
    public List<Device> findAll() {
        return iDeviceJpaRepository.findAll()
                .stream()
                .map(deviceMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Device> findAllByDeletedFalse() {
        return iDeviceJpaRepository.findByDeletedFalse()
                .stream()
                .map(deviceMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Device> findAllById(List<Integer> ids) {
        return iDeviceJpaRepository.findAllById(ids)
                .stream()
                .map(deviceMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Long> getDeviceCountsByType() {
        return iDeviceJpaRepository.countDevicesByType();
    }

    @Override
    public void deleteById(Integer id) {
        iDeviceJpaRepository.deleteById(id);
    }

    @Override
    public BigDecimal getTotalInventoryValue() {
        return iDeviceJpaRepository.getTotalInventoryValue();
    }

    @Override
    public boolean existsBySerial(String serial) {
        return iDeviceJpaRepository.existsBySerial(serial);
    }
}
