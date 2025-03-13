package com.pti_sa.inventory_system.application;

import com.pti_sa.inventory_system.application.dto.response.DeviceResponseDTO;
import com.pti_sa.inventory_system.domain.model.Device;
import com.pti_sa.inventory_system.domain.port.IDeviceRepository;
import com.pti_sa.inventory_system.infrastructure.mapper.DeviceMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeviceService {
    private final IDeviceRepository iDeviceRepository;
    private final DeviceMapper deviceMapper;

    public DeviceService(IDeviceRepository iDeviceRepository, DeviceMapper deviceMapper) {
        this.iDeviceRepository = iDeviceRepository;
        this.deviceMapper = deviceMapper;
    }

    // Guardar un dispositivo
    public DeviceResponseDTO saveDevice(Device device) {
        device.createAudit(device.getCreatedBy());
        Device savedDevice = iDeviceRepository.save(device);
        return deviceMapper.toResponseDTO(savedDevice);
    }

    // Actualizar un dispositivo
    public DeviceResponseDTO updateDevice(Device device) {
        device.updateAudit(device.getUpdatedBy());
        Device updatedDevice = iDeviceRepository.update(device);
        return deviceMapper.toResponseDTO(updatedDevice);
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
    public Optional<DeviceResponseDTO> findDeviceByCode(String code) {
        return iDeviceRepository.findByCode(code)
                .map(deviceMapper::toResponseDTO);
    }

    // Buscar un dispositivo por serial
    public Optional<DeviceResponseDTO> findDeviceBySerial(String serial) {
        return iDeviceRepository.findBySerial(serial)
                .map(deviceMapper::toResponseDTO);
    }

    // Buscar un dispositivo por su estado
    public List<DeviceResponseDTO> findDevicesByStatus(Integer statusId) {
        return iDeviceRepository.findByStatusId(statusId)
                .stream()
                .map(deviceMapper::toResponseDTO)
                .collect(Collectors.toList());
    }


    // Obtener todos los dispositivos
    public List<DeviceResponseDTO> findAllDevices() {
        return iDeviceRepository.findAllByDeletedFalse()
                .stream()
                .map(deviceMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Eliminar un dispositivo por su ID
    public void deleteDeviceById(Integer id) {
        Device device = iDeviceRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
        device.setDeleted(true);
        iDeviceRepository.save(device);
    }

    // Método para obtener la cantidad de dispositivos por tipo
    public Map<String, Long> getDeviceCountsByType() {
        return iDeviceRepository.findAllByDeletedFalse()
                .stream()
                .collect(Collectors.groupingBy(Device::getType, Collectors.counting()));
    }
}
