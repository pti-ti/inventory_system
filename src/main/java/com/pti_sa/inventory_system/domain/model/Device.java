package com.pti_sa.inventory_system.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Device {
    private Integer id;
    private String code;
    private Brand brand;
    private Model model;
    private User user;
    private String serial;
    private String type;
    private String specification;
    private String note;
    private Status status;
    private Location location;
    private BigDecimal price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer updatedBy;
    private boolean deleted = false;


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

    public void updateStatus(Status newStatus){
        this.status = newStatus;
    }

}
