package com.pti_sa.inventory_system.infrastructure.rest;

import com.pti_sa.inventory_system.application.ModelService;
import com.pti_sa.inventory_system.application.dto.response.ModelResponseDTO;
import com.pti_sa.inventory_system.domain.model.Model;
import com.pti_sa.inventory_system.infrastructure.service.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin/models")
public class ModelController {

    private final ModelService modelService;

    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }

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

    // Obtener todos los modelos
    @GetMapping
    public ResponseEntity<List<ModelResponseDTO>> getAllModels(){
        List<ModelResponseDTO> models = modelService.findAllModels();
        return ResponseEntity.ok(models);
    }

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

    // Obtener Model por id
    @GetMapping("/{id}")
    public ResponseEntity<ModelResponseDTO>getModelById(@PathVariable Integer id){
        Optional<ModelResponseDTO> model = modelService.findModelById(id);
        return model.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Obtener la cantidad de dispositivos por model
    @GetMapping("/device-model-count")
    public ResponseEntity<Map<String, Long>> getDeviceCountByModel(){
        Map<String, Long> modelsCount = modelService.countDevicesByModel();
        return ResponseEntity.ok(modelsCount);
    }

    // Eliminar un modelo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModel(@PathVariable Integer id){
        modelService.deleteModelById(id);
        return ResponseEntity.noContent().build();
    }
}
