package com.pti_sa.inventory_system.application.dto.response;

import com.pti_sa.inventory_system.domain.model.Location;
import com.pti_sa.inventory_system.domain.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusResponseDTO {
    private Integer id;
    private String name;
    private Integer createdBy;
    private LocalDateTime createdAt;

    public StatusResponseDTO(Status status) {
        this.id = status.getId();
        this.name = status.getName();
        this.createdBy = status.getCreatedBy();
        this.createdAt = status.getCreatedAt();
    }

}
