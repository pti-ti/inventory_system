package com.pti_sa.inventory_system.application;

import com.pti_sa.inventory_system.application.dto.request.DeviceRequestDTO;
import com.pti_sa.inventory_system.application.dto.response.DeviceResponseDTO;
import com.pti_sa.inventory_system.domain.model.*;
import com.pti_sa.inventory_system.domain.port.*;
import com.pti_sa.inventory_system.infrastructure.mapper.DeviceMapper;
import com.pti_sa.inventory_system.infrastructure.service.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeviceService {
    private static final Logger logger = LoggerFactory.getLogger(DeviceService.class);
    private final IBrandRepository iBrandRepository;
    private final ILocationRepository iLocationRepository;
    private final IStatusRepository iStatusRepository;
    private final IModelRepository iModelRepository;
    private final IDeviceRepository iDeviceRepository;
    private final IUserRepository iUserRepository;
    private final DeviceMapper deviceMapper;
    private final LogbookService logbookService;

    public DeviceService(IBrandRepository iBrandRepository, ILocationRepository iLocationRepository,
            IStatusRepository iStatusRepository, IModelRepository iModelRepository, IDeviceRepository iDeviceRepository,
            IUserRepository iUserRepository, DeviceMapper deviceMapper, LogbookService logbookService) {
        this.iBrandRepository = iBrandRepository;
        this.iLocationRepository = iLocationRepository;
        this.iStatusRepository = iStatusRepository;
        this.iModelRepository = iModelRepository;
        this.iDeviceRepository = iDeviceRepository;
        this.iUserRepository = iUserRepository;
        this.deviceMapper = deviceMapper;
        this.logbookService = logbookService;
    }

    // Guardar un dispositivo
    public DeviceResponseDTO saveDevice(Device device) {
        logger.info("➡️ Recibiendo dispositivo: {}", device);

        // Validación de código duplicado
        Optional<Device> existingDevice = iDeviceRepository.findByCode(device.getCode());
        if (existingDevice.isPresent()) {
            logger.warn("❌ Código duplicado: {}", device.getCode());
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El código del dispositivo \"" + device.getCode() + "\" ya está registrado.");
        }

        // Validar y asignar entidades relacionadas
        Brand brand = iBrandRepository.findById(device.getBrand().getId())
                .orElseThrow(() -> {
                    logger.error("❌ Marca no encontrada: {}", device.getBrand().getId());
                    return new RuntimeException("Marca no encontrada");
                });

        Model model = iModelRepository.findById(device.getModel().getId())
                .orElseThrow(() -> {
                    logger.error("❌ Modelo no encontrado: {}", device.getModel().getId());
                    return new RuntimeException("Modelo no encontrado");
                });

        Status status = iStatusRepository.findById(device.getStatus().getId())
                .orElseThrow(() -> {
                    logger.error("❌ Estado no encontrado: {}", device.getStatus().getId());
                    return new RuntimeException("Estado no encontrado");
                });

        Location location = iLocationRepository.findById(device.getLocation().getId())
                .orElseThrow(() -> {
                    logger.error("❌ Ubicación no encontrada: {}", device.getLocation().getId());
                    return new RuntimeException("Ubicación no encontrada");
                });

        User user = null;
        if (device.getUser() != null && device.getUser().getId() != null) {
            user = iUserRepository.findById(device.getUser().getId())
                    .orElseThrow(() -> {
                        logger.error("❌ Usuario no encontrado: {}", device.getUser().getId());
                        return new RuntimeException("Usuario no encontrado");
                    });
        } else {
            logger.error("❌ Usuario no enviado o id nulo");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario no enviado o id nulo");
        }

        // Asignar entidades recuperadas al dispositivo
        device.setBrand(brand);
        device.setModel(model);
        device.setStatus(status);
        device.setLocation(location);
        device.setUser(user);

        device.setType(device.getType().trim());

        // Auditoría y guardado
        device.createAudit(device.getCreatedBy());
        Device savedDevice = iDeviceRepository.save(device);

        // Crear bitácora automáticamente al crear el dispositivo
        Logbook logbook = new Logbook();
        logbook.setDevice(savedDevice);
        logbook.setBrand(savedDevice.getBrand());
        logbook.setModel(savedDevice.getModel());
        logbook.setStatus(savedDevice.getStatus());
        logbook.setLocation(savedDevice.getLocation());
        logbook.setUser(savedDevice.getUser());
        logbook.setNote("Registro automático de creación de dispositivo");
        logbook.setCreatedBy(savedDevice.getCreatedBy());
        logbookService.saveLogbook(logbook);

        logger.info("✅ Dispositivo guardado: {}", savedDevice);
        return deviceMapper.toResponseDTO(savedDevice);
    }

    // Actualizar un dispositivo
    public DeviceRequestDTO updateDevice(Device device) {
        device.updateAudit(device.getUpdatedBy());
        Device updatedDevice = iDeviceRepository.update(device);

        // Obtener usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer updatedBy = null;
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails) {
                updatedBy = ((CustomUserDetails) principal).getId();
            }
        }
        if (updatedBy == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "No se pudo obtener el usuario autenticado para la bitácora");
        }

        // Crear bitácora automáticamente al editar el dispositivo
        Logbook logbook = new Logbook();
        logbook.setDevice(updatedDevice);
        logbook.setBrand(updatedDevice.getBrand());
        logbook.setModel(updatedDevice.getModel());
        logbook.setStatus(updatedDevice.getStatus());
        logbook.setLocation(updatedDevice.getLocation());
        logbook.setUser(updatedDevice.getUser());
        logbook.setNote("Registro automático de edición de dispositivo");
        logbook.setCreatedBy(updatedBy);
        logbookService.saveLogbook(logbook);
        return deviceMapper.toRequestDTO(updatedDevice);
    }

    // Buscar un dispositivo por ID
    public Optional<DeviceResponseDTO> findDeviceById(Integer id) {
        return iDeviceRepository.findById(id)
                .map(deviceMapper::toResponseDTO);
    }

    // Buscar un dispositivo por código
    public Optional<DeviceRequestDTO> findDeviceByCode(String code) {
        return iDeviceRepository.findByCode(code)
                .map(deviceMapper::toRequestDTO);
    }

    // Buscar un dispositivo por serial
    public Optional<DeviceRequestDTO> findDeviceBySerial(String serial) {
        return iDeviceRepository.findBySerial(serial)
                .map(deviceMapper::toRequestDTO);
    }

    // Buscar un dispositivo por su estado
    public List<DeviceRequestDTO> findDevicesByStatus(Integer statusId) {
        return iDeviceRepository.findByStatusId(statusId)
                .stream()
                .map(deviceMapper::toRequestDTO)
                .collect(Collectors.toList());
    }

    // Obtener todos los dispositivos
    public List<DeviceResponseDTO> findAllDevices() {
        return iDeviceRepository.findAllByDeletedFalse()
                .stream()
                .map(deviceMapper::toResponseDTO) // ✅ Asegúrate de que este método acepte DeviceEntity
                .collect(Collectors.toList());
    }

    // Eliminar un dispositivo por su ID
    public void deleteDeviceById(Integer id) {
        Device device = iDeviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
        device.setDeleted(true);
        iDeviceRepository.save(device);
    }

    // Método para obtener el dispositivo por su código ...
    public List<DeviceResponseDTO> findDevicesByCode(String code) {
        return iDeviceRepository.findByCodeContainingIgnoreCase(code)
                .stream()
                .map(deviceMapper::toResponseDTO)
                .toList();
    }

    // Método para obtener la cantidad de dispositivos por tipo
    public Map<String, Long> getDeviceCountsByType() {
        return iDeviceRepository.findAllByDeletedFalse()
                .stream()
                .collect(Collectors.groupingBy(Device::getType, Collectors.counting()));
    }

    // Método para calcular el valor total del inventario
    public BigDecimal getTotalInventoryValue() {
        return iDeviceRepository.getTotalInventoryValue();
    }
}
