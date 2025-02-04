package com.pti_sa.inventory_system.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Device {
    private Integer id;
    private String code;
    private String name;
    private String serial;
    private String specification;
    private Integer statusId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @Getter
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
