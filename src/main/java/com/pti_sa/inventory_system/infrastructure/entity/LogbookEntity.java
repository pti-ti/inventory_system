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

    @Column(name = "id_device", nullable = false)
    private Integer deviceId;

    @Column(name = "id_user", nullable = false)
    private Integer userId;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private StatusEntity status;


    @Column(name = "location_id", nullable = false)
    private Integer locationId;

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
