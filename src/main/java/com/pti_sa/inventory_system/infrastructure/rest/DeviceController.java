package com.pti_sa.inventory_system.infrastructure.rest;

import com.pti_sa.inventory_system.application.DeviceService;
import com.pti_sa.inventory_system.application.dto.request.DeviceRequestDTO;
import com.pti_sa.inventory_system.application.dto.response.DeviceResponseDTO;
import com.pti_sa.inventory_system.application.dto.response.LastDeviceActionResponseDTO;
import com.pti_sa.inventory_system.domain.model.Device;
import com.pti_sa.inventory_system.infrastructure.service.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/devices")
@Tag(name = "Dispositivos", description = "Operaciones relacionadas con dispositivos")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    // Crear dispositivo
    @Operation(summary = "Registrar un nuevo dispositivo", description = "Crea un nuevo dispositivo en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dispositivo creado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "400", description = "Error en la validación")
    })
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

    @Operation(summary = "Actualizar dispositivo", description = "Actualiza la información de un dispositivo existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dispositivo actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Dispositivo no encontrado")
    })

    // Actualizar dispositivo
    @PutMapping("/{id}")
    public ResponseEntity<DeviceRequestDTO> updateDevice(@PathVariable Integer id, @RequestBody Device device) {
        device.setId(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Object principal = authentication.getPrincipal();
        Integer updatedBy = null;

        if (principal instanceof CustomUserDetails) {
            updatedBy = ((CustomUserDetails) principal).getId();
        }

        if (updatedBy == null) {
            return ResponseEntity.badRequest().body(null);
        }

        // Pasa el usuario autenticado al servicio
        return ResponseEntity.ok(deviceService.updateDevice(device, updatedBy));
    }

    @Operation(summary = "Obtener dispositivo por ID", description = "Devuelve la información de un dispositivo por su ID. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dispositivo encontrado"),
            @ApiResponse(responseCode = "404", description = "Dispositivo no encontrado")
    })
    // Obtener dispositivo por id
    @GetMapping("/{id}")
    public ResponseEntity<DeviceResponseDTO> getDeviceById(@PathVariable Integer id) {
        return deviceService.findDeviceById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar todos los dispositivos", description = "Devuelve todos los dispositivos registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de dispositivos obtenida"),
            @ApiResponse(responseCode = "204", description = "Sin contenido")
    })
    // Obtener todos los dispositivos
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

    @Operation(summary = "Obtener dispositivos por estado", description = "Devuelve dispositivos que coinciden con un estado dado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dispositivos encontrados"),
            @ApiResponse(responseCode = "204", description = "Sin resultados")
    })
    // Obtener dispositivos por estado
    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<DeviceRequestDTO>> getDevicesByStatus(@PathVariable Integer statusId) {
        List<DeviceRequestDTO> devices = deviceService.findDevicesByStatus(statusId);
        return devices.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(devices);
    }

    @Operation(summary = "Contar dispositivos por tipo", description = "Devuelve un mapa con la cantidad de dispositivos agrupados por tipo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conteo obtenido correctamente"),
            @ApiResponse(responseCode = "204", description = "Sin datos")
    })
    // Cantidad de dispositivos por tipo
    @GetMapping("/count-by-type")
    public ResponseEntity<Map<String, Long>> getDeviceCountByType() {
        Map<String, Long> counts = deviceService.getDeviceCountsByType();
        return counts.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(counts);
    }

    @Operation(summary = "Valor total del inventario", description = "Calcula el valor total de los dispositivos en el inventario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Valor calculado correctamente"),
            @ApiResponse(responseCode = "204", description = "Sin datos")
    })
    // Valor total del inventario
    @GetMapping("/total-inventory-value")
    public ResponseEntity<BigDecimal> getTotalInventoryValue() {
        BigDecimal totalValue = deviceService.getTotalInventoryValue();
        return totalValue.compareTo(BigDecimal.ZERO) > 0 ?
                ResponseEntity.ok(totalValue) :
                ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar dispositivo por código", description = "Busca dispositivos cuyo código coincida parcial o totalmente con el parámetro proporcionado. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dispositivos encontrados"),
            @ApiResponse(responseCode = "204", description = "Sin resultados")
    })
    // Buscar dispositivos por código (para el autocomplete)
    @GetMapping("/search")
    public ResponseEntity<List<DeviceResponseDTO>> searchDevicesByCode(@RequestParam("code") String code) {
        List<DeviceResponseDTO> devices = deviceService.findDevicesByCode(code);
        return devices.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(devices);
    }

    @Operation(summary = "Eliminar dispositivo", description = "Elimina un dispositivo por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Dispositivo eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Dispositivo no encontrado")
    })
    // Eliminar un dispositivo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Integer id){
        deviceService.deleteDeviceById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener último dispositivo gestionado", description = "Devuelve la información del último dispositivo gestionado (modificado o agregado).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Último dispositivo encontrado"),
            @ApiResponse(responseCode = "404", description = "No se encontró información")
    })
    @GetMapping("/last-modified")
    public ResponseEntity<LastDeviceActionResponseDTO> getLastModifiedDevice() {
        LastDeviceActionResponseDTO lastDevice = deviceService.getLastModifiedDevice();
        return lastDevice != null ? ResponseEntity.ok(lastDevice) : ResponseEntity.notFound().build();
    }

}
