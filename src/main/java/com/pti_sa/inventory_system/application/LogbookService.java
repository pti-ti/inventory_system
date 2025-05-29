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

        User user = iUserRepository.findById(logbook.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 🔥 Asignar valores recuperados al logbook
        logbook.setDevice(device);
        logbook.setBrand(brand);
        logbook.setModel(model);
        logbook.setStatus(status);
        logbook.setLocation(location);
        logbook.setUser(user);

        // 🔥 Actualizar el dispositivo con los nuevos valores de status y location
        device.setStatus(status);
        device.setLocation(location);
        iDeviceRepository.save(device); // Guardar cambios en el dispositivo

        // Asignar auditoría
        logbook.createAudit(logbook.getCreatedBy());

        // Guardar en la base de datos
        Logbook savedLogbook = iLogbookRepository.save(logbook);

        log.info("🚀 savedLogbook: {}", savedLogbook);

        return logbookMapper.toResponseDTO(savedLogbook);
    }



    public LogbookResponseDTO updateLogbook(Logbook logbook) {
        Logbook existingLogbook = iLogbookRepository.findById(logbook.getId())
                .orElseThrow(() -> new RuntimeException("Bitácora no encontrada"));

        System.out.println("📌 Datos recibidos en updateLogbook: " + logbook);

        // 🔹 Recuperar el dispositivo si está ausente en el logbook
        if (logbook.getDevice() == null) {
            logbook.setDevice(existingLogbook.getDevice());
            System.out.println("⚠️ Dispositivo recuperado del logbook existente: " + logbook.getDevice().getId());
        }

        // 🔹 Obtener el dispositivo desde la base de datos
        Device device = iDeviceRepository.findById(logbook.getDevice().getId())
                .orElseThrow(() -> new RuntimeException("Dispositivo no encontrado con ID: " + logbook.getDevice().getId()));


        if (logbook.getNote() != null) {
            existingLogbook.setNote(logbook.getNote());
        }

        // 🔹 Actualizar información del dispositivo en la bitácora
        existingLogbook.setDevice(device);
        existingLogbook.setBrand(device.getBrand());
        existingLogbook.setModel(device.getModel());

        // 🔹 Verificar si cambió el estado o la ubicación
        boolean statusChanged = logbook.getStatus() != null && !logbook.getStatus().getId().equals(device.getStatus().getId());
        boolean locationChanged = logbook.getLocation() != null && !logbook.getLocation().getId().equals(device.getLocation().getId());

        if (statusChanged || locationChanged) {
            System.out.println("📌 Actualizando dispositivo en base de datos...");

            // 🔹 Actualizar estado en el dispositivo
            if (statusChanged) {
                device.setStatus(logbook.getStatus());
                existingLogbook.setStatus(logbook.getStatus());
                System.out.println("✅ Estado del dispositivo actualizado: " + logbook.getStatus().getId());
            }

            // 🔹 Actualizar ubicación en el dispositivo
            if (locationChanged) {
                device.setLocation(logbook.getLocation());
                existingLogbook.setLocation(logbook.getLocation());
                System.out.println("✅ Ubicación del dispositivo actualizada: " + logbook.getLocation().getId());
            }

            // 🔹 Guardar cambios en el dispositivo
            iDeviceRepository.save(device);
            System.out.println("✅ Dispositivo actualizado en la BD.");
        }

        // 🔹 Guardar cambios en la bitácora
        Logbook updated = iLogbookRepository.update(existingLogbook);

        // 🔹 Convertir a DTO
        LogbookResponseDTO response = logbookMapper.toResponseDTO(updated);

        System.out.println("📌 Bitácora actualizada: " + response);
        return response;
    }

    public LogbookResponseDTO findLatestLogbook(){
        return iLogbookRepository.findLatestLogbook()
                .map(logbookMapper::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("No hay bitácoras registradas."));
    }

    // Buscar un registro de bitácora por su id
    public Optional<LogbookResponseDTO> findLogbookById(Integer id) {
        return iLogbookRepository.findById(id).map(logbookMapper::toResponseDTO);
    }

    public LogbookResponseDTO findById(Long id) {
        System.out.println("Buscando bitácora con id: " + id);
        Optional<Logbook> logbookOpt = iLogbookRepository.findById(id.intValue());
        if (logbookOpt.isPresent()) {
            Logbook logbook = logbookOpt.get();
            System.out.println("Logbook encontrado: " + logbook);
            LogbookResponseDTO dto = logbookMapper.toResponseDTO(logbook);
            System.out.println("DTO generado: " + dto);
            return dto;
        } else {
            throw new RuntimeException("Bitácora no encontrada con id: " + id);
        }
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
