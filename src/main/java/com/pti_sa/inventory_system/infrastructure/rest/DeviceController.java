package com.pti_sa.inventory_system.infrastructure.rest;

import com.pti_sa.inventory_system.application.DeviceService;
import com.pti_sa.inventory_system.application.dto.request.DeviceRequestDTO;
import com.pti_sa.inventory_system.application.dto.response.DeviceResponseDTO;
import com.pti_sa.inventory_system.domain.model.Device;
import com.pti_sa.inventory_system.infrastructure.service.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    // Crear dispositivo
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @PostMapping("/register")
    public ResponseEntity<DeviceResponseDTO> createDevice(@RequestBody Device device) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Object principal = authentication.getPrincipal();
        Integer createdBy = null;

        if (principal instanceof CustomUserDetails) {
            createdBy = ((CustomUserDetails) principal).getId();
        }

        if (createdBy == null) {
            return ResponseEntity.badRequest().body(null);
        }

        device.createAudit(createdBy);
        DeviceResponseDTO savedDevice = deviceService.saveDevice(device);
        return ResponseEntity.ok(savedDevice);
    }

    // Actualizar dispositivo
    @PutMapping("/{id}")
    public ResponseEntity<DeviceRequestDTO>updateDevice(@PathVariable Integer id, @RequestBody Device device){
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
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @GetMapping
    public ResponseEntity<List<DeviceResponseDTO>> getAllDevices() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Object principal = authentication.getPrincipal();
        Integer requestedBy = null;

        if (principal instanceof CustomUserDetails) {
            requestedBy = ((CustomUserDetails) principal).getId();
        }

        if (requestedBy == null) {
            return ResponseEntity.badRequest().build();
        }

        List<DeviceResponseDTO> devices = deviceService.findAllDevices();
        return devices.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(devices);
    }

    // Obtener dispositivos por estado
    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<DeviceRequestDTO>> getDevicesByStatus(@PathVariable Integer statusId) {
        List<DeviceRequestDTO> devices = deviceService.findDevicesByStatus(statusId);
        return devices.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(devices);
    }

    @GetMapping("/count-by-type")
    public ResponseEntity<Map<String, Long>> getDeviceCountByType() {
        Map<String, Long> counts = deviceService.getDeviceCountsByType();
        return counts.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(counts);
    }


    @GetMapping("/total-inventory-value")
    public ResponseEntity<BigDecimal> getTotalInventoryValue() {
        BigDecimal totalValue = deviceService.getTotalInventoryValue();
        return totalValue.compareTo(BigDecimal.ZERO) > 0 ?
                ResponseEntity.ok(totalValue) :
                ResponseEntity.noContent().build();
    }

    // Buscar dispositivos por código (para el autocomplete)
    @GetMapping("/search")
    public ResponseEntity<List<DeviceResponseDTO>> searchDevicesByCode(@RequestParam("code") String code) {
        List<DeviceResponseDTO> devices = deviceService.findDevicesByCode(code);
        return devices.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(devices);
    }

    // Eliminar un dispositivo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Integer id){
        deviceService.deleteDeviceById(id);
        return ResponseEntity.noContent().build();
    }

}
