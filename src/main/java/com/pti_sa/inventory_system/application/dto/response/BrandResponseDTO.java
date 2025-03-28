package com.pti_sa.inventory_system.application.dto.response;

import com.pti_sa.inventory_system.domain.model.Brand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandResponseDTO {
    private Integer id;
    private String name;
    private Integer createdBy;
    private LocalDateTime createdAt;

    public BrandResponseDTO(Brand brand){
        this.id = brand.getId();
        this.name = brand.getName();
        this.createdBy = brand.getCreatedBy();
        this.createdAt = brand.getCreatedAt();
    }
}
