package com.pti_sa.inventory_system.application.dto.response;

import com.pti_sa.inventory_system.domain.model.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelResponseDTO {
    private Integer id;
    private String name;
    private Integer createdBy;
    private LocalDateTime createdAt;

    public ModelResponseDTO(Model model) {
        this.id = model.getId();
        this.name = model.getName();
        this.createdBy = model.getCreatedBy();
        this.createdAt = model.getCreatedAt();
    }
}
