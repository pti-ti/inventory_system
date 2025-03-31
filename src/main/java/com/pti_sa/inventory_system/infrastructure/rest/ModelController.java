package com.pti_sa.inventory_system.infrastructure.rest;

import com.pti_sa.inventory_system.application.ModelService;
import com.pti_sa.inventory_system.application.dto.response.ModelResponseDTO;
import com.pti_sa.inventory_system.domain.model.Model;
import com.pti_sa.inventory_system.infrastructure.service.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/models")
public class ModelController {

    private final ModelService modelService;

    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }

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

        ModelResponseDTO savedModel = modelService.saveModel(model, createdBy);
        return ResponseEntity.ok(savedModel);
    }
}
