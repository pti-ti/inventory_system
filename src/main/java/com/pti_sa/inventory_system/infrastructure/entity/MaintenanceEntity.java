package com.pti_sa.inventory_system.infrastructure.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name= "maintenances")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_device", nullable = false)
    private DeviceEntity device;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "maintenance_type", nullable = false)
    private String maintenanceType;

    @Column(name = "maintenance_date", nullable = false)
    private LocalDate maintenanceDate;

    @Column(name = "comment")
    private String comment;

    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", nullable = false, updatable = false)
    private Integer createdBy;

    @Column(name = "updated_by")
    private Integer updatedBy;
}
