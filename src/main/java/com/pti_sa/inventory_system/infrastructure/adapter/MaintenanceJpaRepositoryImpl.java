package com.pti_sa.inventory_system.infrastructure.adapter;

import com.pti_sa.inventory_system.domain.model.Maintenance;
import com.pti_sa.inventory_system.domain.port.IMaintenanceRepository;
import com.pti_sa.inventory_system.infrastructure.entity.MaintenanceEntity;
import com.pti_sa.inventory_system.infrastructure.mapper.MaintenanceMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MaintenanceJpaRepositoryImpl implements IMaintenanceRepository {

    private final IMaintenanceJpaRepository iMaintenanceJpaRepository;
    private final MaintenanceMapper maintenanceMapper;

    public MaintenanceJpaRepositoryImpl(IMaintenanceJpaRepository iMaintenanceJpaRepository, MaintenanceMapper maintenanceMapper) {
        this.iMaintenanceJpaRepository = iMaintenanceJpaRepository;
        this.maintenanceMapper = maintenanceMapper;
    }

    @Override
    public Maintenance save(Maintenance maintenance) {
        MaintenanceEntity entity = maintenanceMapper.toEntity(maintenance);
        return maintenanceMapper.toModel(iMaintenanceJpaRepository.save(entity));
    }

    @Override
    public Maintenance update(Maintenance maintenance) {
        MaintenanceEntity entity = maintenanceMapper.toEntity(maintenance);
        return maintenanceMapper.toModel(iMaintenanceJpaRepository.save(entity));
    }

    @Override
    public Optional<Maintenance> findById(Integer id) {
        return iMaintenanceJpaRepository.findById(id).map(maintenanceMapper::toModel);
    }

    @Override
    public void deleteById(Integer id) {
        iMaintenanceJpaRepository.deleteById(id);
    }

    @Override
    public List<Maintenance> findAll() {
        return iMaintenanceJpaRepository.findAll()
                .stream()
                .map(maintenanceMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Maintenance> findByDeviceId(Integer deviceId) {
        return iMaintenanceJpaRepository.findByDeviceId(deviceId)
                .stream()
                .map(maintenanceMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Maintenance> findByType(String type) {
        return iMaintenanceJpaRepository.findByMaintenanceType(type)
                .stream()
                .map(maintenanceMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Maintenance> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return iMaintenanceJpaRepository.findByCreatedAtBetween(startDate, endDate)
                .stream()
                .map(maintenanceMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Maintenance> findLatestMaintenance() {
        return iMaintenanceJpaRepository.findTopByOrderByMaintenanceDateDesc()
                .map(maintenanceMapper::toModel);
    }
}
