package com.pti_sa.inventory_system.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Maintenance {
    private Integer id;
    private Device device;
    private User user;
    private String maintenanceType;
    private LocalDate maintenanceDate;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer updatedBy;

    public void createAudit(Integer createdBy){
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        this.createdBy = createdBy;
        this.updatedBy = createdBy;
    }

    public void updateAudit(Integer updatedBy) {
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = updatedBy;
    }
}
