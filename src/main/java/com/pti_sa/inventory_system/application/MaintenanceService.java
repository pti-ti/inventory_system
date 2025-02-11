package com.pti_sa.inventory_system.application;

import com.pti_sa.inventory_system.application.dto.response.MaintenanceResponseDTO;
import com.pti_sa.inventory_system.domain.model.Maintenance;
import com.pti_sa.inventory_system.domain.port.IMaintenanceRepository;
import com.pti_sa.inventory_system.infrastructure.mapper.MaintenanceMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MaintenanceService {
    private final IMaintenanceRepository iMaintenanceRepository;
    private final MaintenanceMapper maintenanceMapper;

    public MaintenanceService(IMaintenanceRepository iMaintenanceRepository, MaintenanceMapper maintenanceMapper) {
        this.iMaintenanceRepository = iMaintenanceRepository;
        this.maintenanceMapper = maintenanceMapper;
    }

    // Guardar un mantenimiento
    public MaintenanceResponseDTO saveMaintenance(Maintenance maintenance) {
        maintenance.createAudit(maintenance.getCreatedBy());
        Maintenance saved = iMaintenanceRepository.save(maintenance);
        return maintenanceMapper.toDto(saved);
    }

    // Actualizar un mantenimiento
    public MaintenanceResponseDTO updateMaintenance(Maintenance maintenance) {
        maintenance.updateAudit(maintenance.getUpdatedBy());
        Maintenance updated = iMaintenanceRepository.update(maintenance);
        return maintenanceMapper.toDto(updated);
    }

    // Buscar un mantenimiento por su ID
    public Optional<MaintenanceResponseDTO> findMaintenanceById(Integer id) {
        return iMaintenanceRepository.findById(id).map(maintenanceMapper::toDto);
    }

    // Obtener todos los mantenimientos
    public List<MaintenanceResponseDTO> findAllMaintenances() {
        return iMaintenanceRepository.findAll()
                .stream()
                .map(maintenanceMapper::toDto)
                .collect(Collectors.toList());
    }

    // Buscar mantenimientos por el ID del dispositivo
    public List<MaintenanceResponseDTO> findMaintenancesByDeviceId(Integer deviceId) {
        return iMaintenanceRepository.findByDeviceId(deviceId)
                .stream()
                .map(maintenanceMapper::toDto)
                .collect(Collectors.toList());
    }

    // Eliminar un mantenimiento por su ID
    public void deleteMaintenanceById(Integer id) {
        iMaintenanceRepository.deleteById(id);
    }
}
