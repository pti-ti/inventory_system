package com.pti_sa.inventory_system.infrastructure.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name= "logbooks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogbookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_device", nullable = false)
    private DeviceEntity device;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private StatusEntity status;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private LocationEntity location;

    @Column(name = "type")
    private String type;

    @Column(name = "note")
    private String note;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", nullable = false, updatable = false)
    private Integer createdBy;

    @Column(name = "updated_by")
    private Integer updatedBy;
}
