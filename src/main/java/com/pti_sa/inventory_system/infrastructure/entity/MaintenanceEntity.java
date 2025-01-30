package com.pti_sa.inventory_system.infrastructure.entity;

import com.pti_sa.inventory_system.domain.model.Device;
import com.pti_sa.inventory_system.domain.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name= "maintenances")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_device", referencedColumnName = "id")
    private Device device;

    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private User user;

    @Column(name = "maintenance_type")
    private String maintenanceType;

    @Column(name = "maintenance_date")
    private String maintenanceDate;

    private String comment;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "updated_by", referencedColumnName = "id")
    private User updatedBy;
}
