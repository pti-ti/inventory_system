package com.pti_sa.inventory_system.application;

import com.pti_sa.inventory_system.application.dto.request.LogbookRequestDTO;
import com.pti_sa.inventory_system.application.dto.response.LogbookResponseDTO;
import com.pti_sa.inventory_system.domain.model.*;
import com.pti_sa.inventory_system.domain.port.*;
import com.pti_sa.inventory_system.infrastructure.mapper.LogbookMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class LogbookService {

    private static final Logger logger = LoggerFactory.getLogger(LogbookService.class);


    private final ILogbookRepository iLogbookRepository;
    private final IDeviceRepository iDeviceRepository;
    private final IBrandRepository iBrandRepository;
    private final IModelRepository iModelRepository;
    private final IStatusRepository iStatusRepository;
    private final ILocationRepository iLocationRepository;
    private final IUserRepository iUserRepository;
    private final LogbookMapper logbookMapper;

    public LogbookService(ILogbookRepository iLogbookRepository, IDeviceRepository iDeviceRepository, IBrandRepository iBrandRepository, IModelRepository iModelRepository, IStatusRepository iStatusRepository, ILocationRepository iLocationRepository, IUserRepository iUserRepository, LogbookMapper logbookMapper) {
        this.iLogbookRepository = iLogbookRepository;
        this.iDeviceRepository = iDeviceRepository;
        this.iBrandRepository = iBrandRepository;
        this.iModelRepository = iModelRepository;
        this.iStatusRepository = iStatusRepository;
        this.iLocationRepository = iLocationRepository;
        this.iUserRepository = iUserRepository;
        this.logbookMapper = logbookMapper;
    }


    public LogbookResponseDTO saveLogbook(Logbook logbook) {
        log.info("Guardando logbook...");

        // Validar que logbook.getDevice() no sea nulo
        if (logbook.getDevice() == null) {
            throw new IllegalArgumentException("El dispositivo no puede ser nulo.");
        }

        // Recuperar entidades de la base de datos
        Device device = iDeviceRepository.findById(logbook.getDevice().getId())
                .orElseThrow(() -> new RuntimeException("Dispositivo no encontrado"));

        Brand brand = iBrandRepository.findById(logbook.getBrand().getId())
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));

        Model model = iModelRepository.findById(logbook.getModel().getId())
                .orElseThrow(() -> new RuntimeException("Modelo no encontrado"));

        Status status = iStatusRepository.findById(logbook.getStatus().getId())
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));

        Location location = iLocationRepository.findById(logbook.getLocation().getId())
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));

        User user = iUserRepository.findById(logbook.getUser().getId()) // 🔥 FIXED!
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 🔥 Asignar el dispositivo recuperado correctamente
        logbook.setDevice(device);

        // Asignar otras entidades recuperadas
        logbook.setBrand(brand);
        logbook.setModel(model);
        logbook.setStatus(status);
        logbook.setLocation(location);
        logbook.setUser(user);

        // Asignar auditoría
        logbook.createAudit(logbook.getCreatedBy());

        // Guardar en la base de datos
        Logbook savedLogbook = iLogbookRepository.save(logbook);

        logger.info("🚀 savedLogbook: {}", savedLogbook);

        return logbookMapper.toResponseDTO(savedLogbook);
    }


    public LogbookResponseDTO updateLogbook(Logbook logbook) {
        Logbook existingLogbook = iLogbookRepository.findById(logbook.getId())
                .orElseThrow(() -> new RuntimeException("Bitácora no encontrada"));

        System.out.println("📌 Datos recibidos en updateLogbook: " + logbook);

        // Recuperar el dispositivo si está ausente
        if (logbook.getDevice() == null) {
            logbook.setDevice(existingLogbook.getDevice());
            System.out.println("⚠️ Dispositivo recuperado del logbook existente: " + logbook.getDevice().getId());
        }

        // Recuperar el dispositivo desde la base de datos
        Device device = iDeviceRepository.findById(logbook.getDevice().getId())
                .orElseThrow(() -> new RuntimeException("Dispositivo no encontrado con ID: " + logbook.getDevice().getId()));

        // 🔹 Actualizar estado
        if (logbook.getStatus() != null && logbook.getStatus().getId() != null) {
            existingLogbook.setStatus(logbook.getStatus());
        }

        // 🔹 Actualizar ubicación
        if (logbook.getLocation() != null && logbook.getLocation().getId() != null) {
            existingLogbook.setLocation(logbook.getLocation());
        }

        // 🔹 Actualizar nota (Asegurar que se está actualizando correctamente)
        if (logbook.getNote() != null) {
            existingLogbook.setNote(logbook.getNote());
            System.out.println("✅ Nota actualizada: " + logbook.getNote());
        }

        // 🔹 Verificar si cambió el estado o la ubicación del dispositivo
        boolean statusChanged = !device.getStatus().getId().equals(logbook.getStatus().getId());
        boolean locationChanged = !device.getLocation().getId().equals(logbook.getLocation().getId());

        System.out.println("📌 Estado actual del dispositivo: " + device.getStatus().getId());
        System.out.println("📌 Nuevo estado recibido: " + logbook.getStatus().getId());

        // 🔹 Actualizar el estado y ubicación del dispositivo si cambian
        if (statusChanged) {
            device.setStatus(logbook.getStatus());
            System.out.println("✅ Estado del dispositivo actualizado.");
        }

        if (locationChanged) {
            device.setLocation(logbook.getLocation());
            System.out.println("✅ Ubicación del dispositivo actualizada.");
        }

        if (statusChanged || locationChanged) {
            iDeviceRepository.save(device);
            System.out.println("✅ Dispositivo actualizado en la BD.");
        }

        // 🔹 Guardar cambios en la bitácora
        Logbook updated = iLogbookRepository.update(existingLogbook);
        return logbookMapper.toResponseDTO(updated);
    }



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
