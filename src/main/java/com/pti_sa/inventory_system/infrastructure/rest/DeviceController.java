package com.pti_sa.inventory_system.infrastructure.rest;

import com.pti_sa.inventory_system.application.DeviceService;
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
    public ResponseEntity<Device> createDevice(@RequestBody Device device){
        Device createdDevice = deviceService.saveDevice(device);
        return ResponseEntity.ok(createdDevice);
    }

    // Actualizar dispositivo
    @PutMapping("/{id}")
    public ResponseEntity<Device> updateDevice(@PathVariable Integer id, @RequestBody Device device){
        device.setId(id);
        Device updateDevice = deviceService.updateDevice(device);
        return ResponseEntity.ok(updateDevice);
    }

    // Obtener dispositivo por id
    @GetMapping("/{id}")
    public ResponseEntity<Device> getDeviceByIid(@PathVariable Integer id){
        Optional<Device> device = deviceService.findDeviceById(id);
        return device.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Obtener todos los dispositivos
    @GetMapping
    public ResponseEntity<List<Device>> getAllDevices(){
        List<Device> devices = deviceService.findAllDevices();
        return ResponseEntity.ok(devices);
    }

    // Eliminar un dispositivo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Integer id){
        deviceService.deleteDeviceById(id);
        return ResponseEntity.noContent().build();
    }

}
