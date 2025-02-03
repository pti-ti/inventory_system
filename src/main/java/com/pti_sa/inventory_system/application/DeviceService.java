package com.pti_sa.inventory_system.application;

import com.pti_sa.inventory_system.domain.model.Device;
import com.pti_sa.inventory_system.domain.port.IDeviceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceService {
    private final IDeviceRepository iDeviceRepository;

    public DeviceService(IDeviceRepository iDeviceRepository) {
        this.iDeviceRepository = iDeviceRepository;
    }

    // Guardar un dispositivo
    public Device saveDevice(Device device){
        device.createAudit(device.getCreatedBy()); // Auditoría al crear el dispositivo
        return iDeviceRepository.save(device);
    }

    // Actualizar un dispositivo
    public Device updateDevice(Device device){
        device.updateAudit(device.getUpdatedBy()); // Auditoría al actualizar el dispositivo
        return iDeviceRepository.update(device);
    }

    // Buscar un dispositivo por ID
    public Optional<Device> findDeviceById(Integer id){
        return iDeviceRepository.findById(id);
    }

    // Buscar un dispositivo por código
    public Optional<Device> findDeviceByCode(String code){
        return iDeviceRepository.findByCode(code);
    }

    // Obtener todos los dispositivos
    public List<Device> findAllDevices(){
        return iDeviceRepository.findAll();
    }

    // Eliminar un dispositivo por su ID
    public void deleteDeviceById(Integer id){
        iDeviceRepository.deleteById(id);
    }
}
