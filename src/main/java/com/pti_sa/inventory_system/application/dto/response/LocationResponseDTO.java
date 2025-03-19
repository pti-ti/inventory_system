package com.pti_sa.inventory_system.application.dto.response;

import com.pti_sa.inventory_system.domain.model.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationResponseDTO {
    private Integer id;
    private String name;
    private Integer createdBy;
    private LocalDateTime createdAt;

    public LocationResponseDTO(Location location) {
        this.id = location.getId();
        this.name = location.getName();
        this.createdBy = location.getCreatedBy();
        this.createdAt = location.getCreatedAt();
    }
}

