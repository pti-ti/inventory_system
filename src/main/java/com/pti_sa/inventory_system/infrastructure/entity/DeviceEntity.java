package com.pti_sa.inventory_system.infrastructure.entity;

import com.pti_sa.inventory_system.domain.model.Status;
import com.pti_sa.inventory_system.domain.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name= "devices")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "serial", nullable = false, unique = true)
    private String serial;

    @Column(name = "specification")
    private String specification;

    @Column(name = "type", nullable = false)
    private String type;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private StatusEntity status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", nullable = false, updatable = false)
    private Integer createdBy;


    @Column(name = "updated_by")
    private Integer updatedBy;


}
