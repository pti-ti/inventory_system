package com.pti_sa.inventory_system.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Logbook {
    private Integer id;
    private Device device;
    private User user;
    private Status status;
    private Brand brand;
    private Model model;
    private Location location;
    private String type;
    private String note;
    private boolean deleted = false;
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
