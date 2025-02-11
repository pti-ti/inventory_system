package com.pti_sa.inventory_system.application;

import com.pti_sa.inventory_system.application.dto.response.LogbookResponseDTO;
import com.pti_sa.inventory_system.domain.model.Logbook;
import com.pti_sa.inventory_system.domain.port.ILogbookRepository;
import com.pti_sa.inventory_system.infrastructure.mapper.LogbookMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LogbookService {
    private final ILogbookRepository iLogbookRepository;
    private final LogbookMapper logbookMapper;

    public LogbookService(ILogbookRepository iLogbookRepository, LogbookMapper logbookMapper) {
        this.iLogbookRepository = iLogbookRepository;
        this.logbookMapper = logbookMapper;
    }

    // Guardar un registro de bitácora
    public LogbookResponseDTO saveLogbook(Logbook logbook) {
        logbook.createAudit(logbook.getCreatedBy()); // Auditoría
        Logbook savedLogbook = iLogbookRepository.save(logbook);
        return logbookMapper.toResponseDTO(savedLogbook);
    }

    // Actualizar un registro de bitácora
    public LogbookResponseDTO updateLogbook(Logbook logbook) {
        logbook.updateAudit(logbook.getUpdatedBy()); // Auditoría
        Logbook updatedLogbook = iLogbookRepository.update(logbook);
        return logbookMapper.toResponseDTO(updatedLogbook);
    }

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

    // Eliminar un registro de bitácora por su ID
    public void deleteLogbookById(Integer id) {
        iLogbookRepository.deleteById(id);

    }
}
