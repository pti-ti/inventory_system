package com.pti_sa.inventory_system.application;

import com.pti_sa.inventory_system.application.dto.response.MaintenanceResponseDTO;
import com.pti_sa.inventory_system.domain.model.Device;
import com.pti_sa.inventory_system.domain.model.Item;
import com.pti_sa.inventory_system.domain.model.Maintenance;
import com.pti_sa.inventory_system.domain.model.User;
import com.pti_sa.inventory_system.domain.port.IDeviceRepository;
import com.pti_sa.inventory_system.domain.port.IItemRepository;
import com.pti_sa.inventory_system.domain.port.IMaintenanceRepository;
import com.pti_sa.inventory_system.domain.port.IUserRepository;
import com.pti_sa.inventory_system.infrastructure.mapper.MaintenanceMapper;
import org.springframework.data.crossstore.ChangeSetPersister;
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
    private final IUserRepository iUserRepository;
    private final IDeviceRepository iDeviceRepository;
    private final IItemRepository iItemRepository;

    public MaintenanceService(IMaintenanceRepository iMaintenanceRepository, MaintenanceMapper maintenanceMapper, IUserRepository iUserRepository, IDeviceRepository iDeviceRepository, IItemRepository iItemRepository) {
        this.iMaintenanceRepository = iMaintenanceRepository;
        this.maintenanceMapper = maintenanceMapper;
        this.iUserRepository = iUserRepository;
        this.iDeviceRepository = iDeviceRepository;
        this.iItemRepository = iItemRepository;
    }

    public MaintenanceResponseDTO saveMaintenance(Maintenance maintenance) {
        User user = iUserRepository.findById(maintenance.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + maintenance.getUser().getId()));

        Device device = iDeviceRepository.findById(maintenance.getDevice().getId())
                .orElseThrow(() -> new RuntimeException("Dispositivo no encontrado con ID: " + maintenance.getDevice().getId()));

        // Obtener los ítems desde la base de datos
        List<Item> items = iItemRepository.findAllById(
                maintenance.getItems().stream().map(Item::getId).toList()
        );

        // Validar que todos los ítems existen
        if (items.size() != maintenance.getItems().size()) {
            throw new RuntimeException("Algunos ítems no existen en la base de datos.");
        }

        // Asignar relaciones al mantenimiento
        maintenance.setUser(user);
        maintenance.setDevice(device);
        maintenance.setItems(items);

        // Establecer auditoría
        maintenance.createAudit(maintenance.getCreatedBy());

        // Guardar el mantenimiento con los ítems
        Maintenance saved = iMaintenanceRepository.save(maintenance);

        MaintenanceResponseDTO response = maintenanceMapper.toDto(saved);

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

    // Ultimo mantenimiento registrado
    public MaintenanceResponseDTO findLatestMaintenance() {
        return iMaintenanceRepository.findLatestMaintenance()
                .map(maintenanceMapper::toDto)
                .orElseThrow(() -> new RuntimeException("No hay mantenimientos registrados."));
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

    // Obtener los mantenimientos con los items...
    public List<Item> findItemsByMaintenanceId(Integer maintenanceId) {
        Maintenance maintenance = iMaintenanceRepository.findById(maintenanceId)
                .orElseThrow(() -> new RuntimeException("Mantenimiento no encontrado con ID: " + maintenanceId));

        return maintenance.getItems(); // Asegurar que la relación está bien configurada
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
