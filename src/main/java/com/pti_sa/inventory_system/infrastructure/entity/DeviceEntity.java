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
    private Long id;

    @Column(name = "cod_device")
    private String code;

    @Column(name = "name_device")
    private String name;

    private String serial;

    private String specification;

    @ManyToOne
    @JoinColumn(name = "id_status", referencedColumnName = "id")
    private Status status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by", referencedColumnName = "id")
    private User updatedBy;


}
