package com.pti_sa.inventory_system.application;

import com.pti_sa.inventory_system.application.dto.response.MaintenanceResponseDTO;
import com.pti_sa.inventory_system.domain.model.Maintenance;
import com.pti_sa.inventory_system.domain.port.IMaintenanceRepository;
import com.pti_sa.inventory_system.infrastructure.mapper.MaintenanceMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        // 1️⃣ Buscar el mantenimiento existente
        Maintenance existingMaintenance = iMaintenanceRepository.findById(maintenance.getId())
                .orElseThrow(() -> new RuntimeException("Mantenimiento no encontrado"));

        // 2️⃣ Asignar los nuevos valores (evitar sobrescribir valores nulos)
        existingMaintenance.setMaintenanceType(maintenance.getMaintenanceType());
        existingMaintenance.setMaintenanceDate(maintenance.getMaintenanceDate());
        existingMaintenance.setComment(maintenance.getComment());
        existingMaintenance.updateAudit(maintenance.getUpdatedBy());

        // 3️⃣ Asegurar que `device` y `user` no sean nulos
        if (maintenance.getDevice() != null && maintenance.getDevice().getId() != null) {
            existingMaintenance.setDevice(maintenance.getDevice());
        }
        if (maintenance.getUser() != null && maintenance.getUser().getId() != null) {
            existingMaintenance.setUser(maintenance.getUser());
        }

        // 4️⃣ Guardar los cambios y devolver el DTO
        Maintenance updated = iMaintenanceRepository.update(existingMaintenance);
        return maintenanceMapper.toDto(updated);
    }

    /*public MaintenanceResponseDTO updateMaintenance(Maintenance maintenance) {
        maintenance.updateAudit(maintenance.getUpdatedBy());
        Maintenance updated = iMaintenanceRepository.update(maintenance);
        return maintenanceMapper.toDto(updated);
    }*/

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

    // Buscar mantenimientos por el tipo
    public List<MaintenanceResponseDTO> findMaintenancesByType(String type){
        return iMaintenanceRepository.findByType(type)
                .stream()
                .map(maintenanceMapper::toDto)
                .collect(Collectors.toList());
    }

    // Buscar mantenimientos por rango de fechas
    public List<MaintenanceResponseDTO> findMaintenancesByDateRange(LocalDate startDate, LocalDate endDate){
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23,59, 59);

        return iMaintenanceRepository.findByCreatedAtBetween(startDateTime, endDateTime)
                .stream()
                .map(maintenanceMapper::toDto)
                .toList();
    }

    // Eliminar un mantenimiento por su ID
    public void deleteMaintenanceById(Integer id) {
        iMaintenanceRepository.deleteById(id);
    }
}
