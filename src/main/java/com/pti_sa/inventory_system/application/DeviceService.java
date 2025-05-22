package com.pti_sa.inventory_system.application;

import com.pti_sa.inventory_system.application.dto.request.DeviceRequestDTO;
import com.pti_sa.inventory_system.application.dto.response.DeviceResponseDTO;
import com.pti_sa.inventory_system.domain.model.*;
import com.pti_sa.inventory_system.domain.port.*;
import com.pti_sa.inventory_system.infrastructure.mapper.DeviceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
    private final DeviceMapper deviceMapper;

    public DeviceService(IBrandRepository iBrandRepository, ILocationRepository iLocationRepository, IStatusRepository iStatusRepository, IModelRepository iModelRepository, IDeviceRepository iDeviceRepository, DeviceMapper deviceMapper) {
        this.iBrandRepository = iBrandRepository;
        this.iLocationRepository = iLocationRepository;
        this.iStatusRepository = iStatusRepository;
        this.iModelRepository = iModelRepository;
        this.iDeviceRepository = iDeviceRepository;
        this.deviceMapper = deviceMapper;
    }

    // Guardar un dispositivo
    public DeviceResponseDTO saveDevice(Device device) {

        // Verificación del código del dispositivo si ya existe
        Optional<Device> existingDevice = iDeviceRepository.findByCode(device.getCode());
        if (existingDevice.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El código del dispositivo ya está registrado.");
        }

        // Obtener entidades completas desde la base de datos antes de asignarlas
        Brand brand = iBrandRepository.findById(device.getBrand().getId())
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));

        Model model = iModelRepository.findById(device.getModel().getId())
                .orElseThrow(() -> new RuntimeException("Modelo no encontrado"));

        Status status = iStatusRepository.findById(device.getStatus().getId())
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));

        Location location = iLocationRepository.findById(device.getLocation().getId())
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));


        // Asignar entidades recuperadas al dispositivo
        device.setBrand(brand);
        device.setModel(model);
        device.setStatus(status);
        device.setLocation(location);

        device.setType(device.getType().trim());

        // Asignar auditoría
        device.createAudit(device.getCreatedBy());

        // Guardar el dispositivo en la base de datos
        Device savedDevice = iDeviceRepository.save(device);

        logger.info("🚀 savedDevice: {}", savedDevice);
        return deviceMapper.toResponseDTO(savedDevice);
    }



    // Actualizar un dispositivo
    public DeviceRequestDTO updateDevice(Device device) {
        device.updateAudit(device.getUpdatedBy());
        Device updatedDevice = iDeviceRepository.update(device);
        return deviceMapper.toRequestDTO(updatedDevice);
    }

//    // Actualizar el status del dispositivo
//    public DeviceResponseDTO updateStatus(Integer deviceId, Integer statusId, String updatedBy){
//        Optional<Device> optionalDevice = iDeviceRepository.findById(deviceId);
//
//        if(optionalDevice.isPresent()){
//            Device device = optionalDevice.get();
//            device.setStatus(new Status(statusId));
//            device.updateAudit(Integer.parseInt(updatedBy));
//            Device updateDevice = iDeviceRepository.update(device);
//            return deviceMapper.toResponseDTO(updateDevice);
//        }
//
//        throw new RuntimeException("El dispositivo con el ID: " + deviceId);
//    }

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
