package com.pti_sa.inventory_system.infrastructure.rest;

import com.pti_sa.inventory_system.application.DeviceService;
import com.pti_sa.inventory_system.application.dto.DeviceResponseDTO;
import com.pti_sa.inventory_system.domain.model.Device;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    // Crear dispositivo
    @PostMapping
    public ResponseEntity<DeviceResponseDTO> createDevice(@RequestBody Device device) {
        return ResponseEntity.ok(deviceService.saveDevice(device));
    }

    // Actualizar dispositivo
    @PutMapping("/{id}")
    public ResponseEntity<DeviceResponseDTO>updateDevice(@PathVariable Integer id, @RequestBody Device device){
        device.setId(id);
        return ResponseEntity.ok(deviceService.updateDevice(device));
    }

    // Obtener dispositivo por id
    @GetMapping("/{id}")
    public ResponseEntity<DeviceResponseDTO> getDeviceById(@PathVariable Integer id) {
        return deviceService.findDeviceById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Obtener todos los dispositivos
    @GetMapping
    public ResponseEntity<List<DeviceResponseDTO>> getAllDevices() {
        List<DeviceResponseDTO> devices = deviceService.findAllDevices();
        return devices.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(devices);
    }

    // Obtener dispositivos por estado
    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<DeviceResponseDTO>> getDevicesByStatus(@PathVariable Integer statusId) {
        List<DeviceResponseDTO> devices = deviceService.findDevicesByStatus(statusId);
        return devices.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(devices);
    }

    // Eliminar un dispositivo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Integer id){
        deviceService.deleteDeviceById(id);
        return ResponseEntity.noContent().build();
    }

}
