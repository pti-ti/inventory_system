package com.pti_sa.inventory_system.application;

import com.pti_sa.inventory_system.application.dto.response.LogbookResponseDTO;
import com.pti_sa.inventory_system.domain.model.Device;
import com.pti_sa.inventory_system.domain.model.Logbook;
import com.pti_sa.inventory_system.domain.model.Maintenance;
import com.pti_sa.inventory_system.domain.model.Status;
import com.pti_sa.inventory_system.domain.port.IDeviceRepository;
import com.pti_sa.inventory_system.domain.port.ILogbookRepository;
import com.pti_sa.inventory_system.infrastructure.mapper.LogbookMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LogbookService {
    private final ILogbookRepository iLogbookRepository;
    private final IDeviceRepository iDeviceRepository;
    private final LogbookMapper logbookMapper;

    public LogbookService(ILogbookRepository iLogbookRepository, IDeviceRepository iDeviceRepository, LogbookMapper logbookMapper) {
        this.iLogbookRepository = iLogbookRepository;
        this.iDeviceRepository = iDeviceRepository;
        this.logbookMapper = logbookMapper;
    }

    // Guardar un registro de bitácora
    public LogbookResponseDTO saveLogbook(Logbook logbook) {
        logbook.createAudit(logbook.getCreatedBy()); // Auditoría

        // Buscar dispositivo y actualizar estado
        Device device = iDeviceRepository.findById(logbook.getDevice().getId())
                .orElseThrow(() -> new IllegalArgumentException("Dispositivo no encontrado con ID: " + logbook.getDevice().getId()));

        device.updateStatus(logbook.getStatus());
        iDeviceRepository.save(device);

        // Guardar logbook
        Logbook savedLogbook = iLogbookRepository.save(logbook);
        return logbookMapper.toResponseDTO(savedLogbook);
//        logbook.createAudit(logbook.getCreatedBy()); // Auditoría
    }


    // Actualizar un registro de bitácora
    public LogbookResponseDTO updateLogbook(Logbook logbook) {

        Logbook existingLogbook = iLogbookRepository.findById(logbook.getId())
                .orElseThrow(() -> new RuntimeException("Bitácora no encontrado"));

        //
        existingLogbook.setNote(logbook.getNote());
        existingLogbook.setCreatedAt(logbook.getCreatedAt());
        existingLogbook.updateAudit(logbook.getUpdatedBy());

        Logbook updated = iLogbookRepository.update(existingLogbook);
        return logbookMapper.toResponseDTO(updated);
    }

    // Actualizar el estado del dispositivo


    // Buscar un registro de bitácora por su id
    public Optional<LogbookResponseDTO> findLogbookById(Integer id) {
        return iLogbookRepository.findById(id).map(logbookMapper::toResponseDTO);
    }

    // Obtener todos los registros de bitácora
    public List<LogbookResponseDTO> findAllByDeletedFalse() {
        return iLogbookRepository.findAllByDeletedFalse().stream()
                .map(logbookMapper::toResponseDTO)
                .toList();
    }

    // Buscar registros de bitácora por DeviceId
    public List<LogbookResponseDTO> findLogbookByDeviceId(Integer deviceId) {
        return iLogbookRepository.findByDeviceId(deviceId).stream()
                .map(logbookMapper::toResponseDTO)
                .toList();
    }

    public List<LogbookResponseDTO> findLogbooksByStatus(String statusName){
        return iLogbookRepository.findByStatusName(statusName).stream()
                .map(logbookMapper::toResponseDTO)
                .toList();
    }

    // Buscar registros de bitácora por UserId
    public List<LogbookResponseDTO> findLogbooksByUserId(Integer userId) {
        return iLogbookRepository.findByUserId(userId).stream()
                .map(logbookMapper::toResponseDTO)
                .toList();
    }

    // Buscar registros de bitácora por LocationId
    public List<LogbookResponseDTO> findLogbooksByLocationId(Integer locationId) {
        return iLogbookRepository.findByLocationId(locationId).stream()
                .map(logbookMapper::toResponseDTO)
                .toList();
    }

    // Buscar registros de bitácora entre dos fechas
    public List<LogbookResponseDTO> findLogbooksByDateRange(LocalDate startDate, LocalDate endDate){
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        return iLogbookRepository.findByCreatedAtBetween(startDateTime, endDateTime)
                .stream()
                .map(logbookMapper::toResponseDTO)
                .toList();
    }

    // Eliminar un registro de bitácora por su ID
    public void deleteLogbookById(Integer id) {
        Logbook logbook = iLogbookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bitácora no encontrada"));

        logbook.setDeleted(true);
        iLogbookRepository.save(logbook);

    }
}
