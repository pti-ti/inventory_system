package com.pti_sa.inventory_system.application;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.*;
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

        if (device.getNote() != null) {
            device.setNote(device.getNote().trim());
        }

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
        logbook.setNote(
                device.getNote() != null && !device.getNote().isBlank()
                        ? device.getNote()
                        : "Registro automático de creación de dispositivo");
        logbook.setCreatedBy(savedDevice.getCreatedBy());
        logbookService.saveLogbook(logbook);

        logger.info("✅ Dispositivo guardado: {}", savedDevice);
        return deviceMapper.toResponseDTO(savedDevice);
    }

    public DeviceRequestDTO updateDevice(Device device) {
        // 1. Obtener el dispositivo original antes de actualizar
        Device originalDevice = iDeviceRepository.findById(device.getId())
                .orElseThrow(() -> new RuntimeException("Dispositivo no encontrado para bitácora"));

        if (device.getNote() != null) {
            device.setNote(device.getNote().trim());
        }

        // 2. Actualizar el dispositivo
        device.updateAudit(device.getUpdatedBy());
        Device updatedDevice = iDeviceRepository.update(device);

        // 3. Obtener usuario autenticado
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

        // 4. Comparar campos y construir la nota de cambios y el detalle
        List<String> cambios = new ArrayList<>();
        Map<String, Map<String, String>> cambiosDetalle = new LinkedHashMap<>();

        if (!Objects.equals(originalDevice.getBrand().getId(), updatedDevice.getBrand().getId())) {
            cambios.add("Marca");
            cambiosDetalle.put("Marca", Map.of(
                    "antes", originalDevice.getBrand().getName(),
                    "despues", updatedDevice.getBrand().getName()
            ));
        }
        if (!Objects.equals(originalDevice.getModel().getId(), updatedDevice.getModel().getId())) {
            cambios.add("Modelo");
            cambiosDetalle.put("Modelo", Map.of(
                    "antes", originalDevice.getModel().getName(),
                    "despues", updatedDevice.getModel().getName()
            ));
        }
        if (!Objects.equals(originalDevice.getStatus().getId(), updatedDevice.getStatus().getId())) {
            cambios.add("Estado");
            cambiosDetalle.put("Estado", Map.of(
                    "antes", originalDevice.getStatus().getName(),
                    "despues", updatedDevice.getStatus().getName()
            ));
        }
        if (!Objects.equals(originalDevice.getLocation().getId(), updatedDevice.getLocation().getId())) {
            cambios.add("Ubicación");
            cambiosDetalle.put("Ubicación", Map.of(
                    "antes", originalDevice.getLocation().getName(),
                    "despues", updatedDevice.getLocation().getName()
            ));
        }
        if (!Objects.equals(originalDevice.getUser().getId(), updatedDevice.getUser().getId())) {
            cambios.add("Usuario");
            cambiosDetalle.put("Usuario", Map.of(
                    "antes", originalDevice.getUser().getEmail(),
                    "despues", updatedDevice.getUser().getEmail()
            ));
        }
        if (!Objects.equals(originalDevice.getType(), updatedDevice.getType())) {
            cambios.add("Tipo");
            cambiosDetalle.put("Tipo", Map.of(
                    "antes", originalDevice.getType(),
                    "despues", updatedDevice.getType()
            ));
        }
        if (!Objects.equals(originalDevice.getCode(), updatedDevice.getCode())) {
            cambios.add("Código");
            cambiosDetalle.put("Código", Map.of(
                    "antes", originalDevice.getCode(),
                    "despues", updatedDevice.getCode()
            ));
        }
        if (!Objects.equals(originalDevice.getSerial(), updatedDevice.getSerial())) {
            cambios.add("Serial");
            cambiosDetalle.put("Serial", Map.of(
                    "antes", originalDevice.getSerial(),
                    "despues", updatedDevice.getSerial()
            ));
        }
        if (!Objects.equals(originalDevice.getNote(), updatedDevice.getNote())) {
            cambios.add("Nota");
            cambiosDetalle.put("Nota", Map.of(
                    "antes", originalDevice.getNote() == null ? "" : originalDevice.getNote(),
                    "despues", updatedDevice.getNote() == null ? "" : updatedDevice.getNote()
            ));
        }

        String note = cambios.isEmpty()
                ? "Registro automático. Sin cambios detectados."
                : "Registro automático. Cambios: " + String.join(", ", cambios) + ".";

        // Serializar el detalle de cambios a JSON
        String cambiosJson = "";
        try {
            cambiosJson = new ObjectMapper().writeValueAsString(cambiosDetalle);
        } catch (Exception e) {
            cambiosJson = "{}";
        }

        // 5. Crear bitácora automáticamente al editar el dispositivo
        Logbook logbook = new Logbook();
        logbook.setDevice(updatedDevice);
        logbook.setBrand(updatedDevice.getBrand());
        logbook.setModel(updatedDevice.getModel());
        logbook.setStatus(updatedDevice.getStatus());
        logbook.setLocation(updatedDevice.getLocation());
        logbook.setUser(updatedDevice.getUser());
        logbook.setNote(note);
        logbook.setChanges(cambiosJson); // <--- Aquí guardas el detalle
        logbook.setCreatedBy(updatedBy);
        logbookService.saveLogbook(logbook);

        return deviceMapper.toRequestDTO(updatedDevice);
    }

    // Buscar un dispositivo por ID
    public Optional<DeviceResponseDTO> findDeviceById(Integer id) {
        return iDeviceRepository.findById(id)
                .map(device -> {
                    DeviceResponseDTO dto = deviceMapper.toResponseDTO(device);
                    // Busca el correo usando el ID
                    dto.setCreatedByEmail(
                            device.getCreatedBy() != null
                                    ? iUserRepository.findById(device.getCreatedBy())
                                    .map(User::getEmail)
                                    .orElse("Desconocido")
                                    : "Desconocido"
                    );
                    return dto;
                });
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
                .map(device -> {
                    DeviceResponseDTO dto = deviceMapper.toResponseDTO(device);
                    dto.setCreatedByEmail(
                            device.getCreatedBy() != null
                                    ? iUserRepository.findById(device.getCreatedBy())
                                    .map(User::getEmail)
                                    .orElse("Desconocido")
                                    : "Desconocido"
                    );
                    return dto;
                })
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
