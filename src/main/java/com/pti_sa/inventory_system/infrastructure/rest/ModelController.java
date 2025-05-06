package com.pti_sa.inventory_system.infrastructure.rest;

import com.pti_sa.inventory_system.application.ModelService;
import com.pti_sa.inventory_system.application.dto.response.ModelResponseDTO;
import com.pti_sa.inventory_system.domain.model.Model;
import com.pti_sa.inventory_system.infrastructure.service.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Tag(name = "Modelos", description = "Operaciones relacionadas con los modelos de dispositivos")
@RequestMapping("/api/v1/models")
public class ModelController {

    private final ModelService modelService;

    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }

    @Operation(summary = "Crear un nuevo modelo", description = "Permite crear un modelo de dispositivo si el usuario está autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modelo creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o usuario no válido"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
    // Crear una nueva marca
    @PostMapping("/create")
    public ResponseEntity<ModelResponseDTO> createModel(@RequestBody Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Object principal = authentication.getPrincipal();
        Integer createdBy = null;
        if (principal instanceof CustomUserDetails){
            createdBy = ((CustomUserDetails) principal).getId();
        }

        if (createdBy == null){
            return ResponseEntity.badRequest().build();
        }

        ModelResponseDTO savedModel = modelService.saveModel(model, createdBy);
        return ResponseEntity.ok(savedModel);
    }

    @Operation(summary = "Actualizar un modelo", description = "Actualiza la información de un modelo existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de modelos obtenida exitosamente")
    })
    // Obtener todos los modelos
    @GetMapping
    public ResponseEntity<List<ModelResponseDTO>> getAllModels(){
        List<ModelResponseDTO> models = modelService.findAllModels();
        return ResponseEntity.ok(models);
    }

    @Operation(summary = "Actualizar un modelo", description = "Actualiza la información de un modelo existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modelo actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o usuario no válido"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
    // Actualizar un modelo
    @PutMapping("/{id}")
    public ResponseEntity<Model> updateModel(@PathVariable Integer id, @RequestBody Model model){
        // Obtener usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null ||  !authentication.isAuthenticated()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Object principal = authentication.getPrincipal();
        Integer updatedBy = null;
        if(principal instanceof CustomUserDetails){
            updatedBy = ((CustomUserDetails) principal).getId();
        }

        if (updatedBy == null){
            return ResponseEntity.badRequest().build();
        }

        // Asignar el ID del usuario que está actualizando
        model.setId(id);
        model.setUpdatedBy(updatedBy);

        Model updatedModel = modelService.updateModel(model);
        return ResponseEntity.ok(updatedModel);
    }

    @Operation(summary = "Obtener modelo por ID", description = "Devuelve los detalles de un modelo específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modelo encontrado"),
            @ApiResponse(responseCode = "404", description = "Modelo no encontrado")
    })
    // Obtener Model por id
    @GetMapping("/{id}")
    public ResponseEntity<ModelResponseDTO>getModelById(@PathVariable Integer id){
        Optional<ModelResponseDTO> model = modelService.findModelById(id);
        return model.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener la cantidad de dispositivos por modelo", description = "Retorna un mapa con el nombre del modelo y la cantidad de dispositivos asociados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conteo obtenido exitosamente")
    })
    // Obtener la cantidad de dispositivos por model
    @GetMapping("/device-model-count")
    public ResponseEntity<Map<String, Long>> getDeviceCountByModel(){
        Map<String, Long> modelsCount = modelService.countDevicesByModel();
        return ResponseEntity.ok(modelsCount);
    }

    @Operation(summary = "Eliminar un modelo por ID", description = "Elimina un modelo registrado en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Modelo eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Modelo no encontrado")
    })
    // Eliminar un modelo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModel(@PathVariable Integer id){
        modelService.deleteModelById(id);
        return ResponseEntity.noContent().build();
    }
}
