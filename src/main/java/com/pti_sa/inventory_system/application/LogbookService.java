package com.pti_sa.inventory_system.application;

import com.pti_sa.inventory_system.application.dto.response.LogbookResponseDTO;
import com.pti_sa.inventory_system.domain.model.Device;
import com.pti_sa.inventory_system.domain.model.Logbook;
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

        // Buscar el dispositivo asociado al logbook
        Optional<Device> optionalDevice = iDeviceRepository.findById(logbook.getDevice().getId());

        if (optionalDevice.isPresent()) {
            Device device = optionalDevice.get();

            // Actualizar el estado del dispositivo
            device.updateStatus(logbook.getStatus());

            // Guardar el dispositivo actualizado en la BD
            iDeviceRepository.save(device);
        } else {
            throw new RuntimeException("Dispositivo no encontrado con ID: " + logbook.getDevice().getId());
        }

        // Guardar el logbook en la BD
        Logbook savedLogbook = iLogbookRepository.save(logbook);
        return logbookMapper.toResponseDTO(savedLogbook);
    }


//    public LogbookResponseDTO saveLogbook(Logbook logbook){
//        Device device = iDeviceRepository.findById(logbook.getDevice().getId())
//                .orElseThrow(() -> new IllegalArgumentException("El dispositivo no existe"));
//
//        if(logbook.getStatus() == null){
//            logbook.setStatus(new Status(1));
//        }
//
//        logbook.createAudit(logbook.getCreatedBy());
//        Logbook savedLogbook = iLogbookRepository.save(logbook);
//        return logbookMapper.toResponseDTO(savedLogbook);
//    }
//    public LogbookResponseDTO saveLogbook(Logbook logbook) {
//        logbook.createAudit(logbook.getCreatedBy()); // Auditoría
//        Logbook savedLogbook = iLogbookRepository.save(logbook);
//        return logbookMapper.toResponseDTO(savedLogbook);
//    }

    // Actualizar un registro de bitácora
    public LogbookResponseDTO updateLogbook(Logbook logbook) {
        logbook.updateAudit(logbook.getUpdatedBy()); // Auditoría
        Logbook updatedLogbook = iLogbookRepository.update(logbook);
        return logbookMapper.toResponseDTO(updatedLogbook);
    }

    // Actualizar el estado del dispositivo


    // Buscar un registro de bitácora por su id
    public Optional<LogbookResponseDTO> findLogbookById(Integer id) {
        return iLogbookRepository.findById(id).map(logbookMapper::toResponseDTO);
    }

    // Obtener todos los registros de bitácora
    public List<LogbookResponseDTO> findAllLogbooks() {
        return iLogbookRepository.findAll().stream()
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

        return iLogbookRepository.findByCreatedAtBetween(startDateTime, endDateTime).stream()
                .map(logbookMapper::toResponseDTO)
                .toList();
    }

    // Eliminar un registro de bitácora por su ID
    public void deleteLogbookById(Integer id) {
        iLogbookRepository.deleteById(id);

    }
}
