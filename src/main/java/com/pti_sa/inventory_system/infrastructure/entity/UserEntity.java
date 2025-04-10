package com.pti_sa.inventory_system.infrastructure.entity;


import com.pti_sa.inventory_system.domain.model.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name= "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    //@Column(nullable = false)
    private UserType userType;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private LocationEntity location;

    @Column(name = "deleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean deleted = false; // Eliminación lógica

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeviceEntity> devices;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", updatable = false) // nullable = false,
    private Integer createdBy;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @PrePersist
    public void prePersist() {
        if (this.createdBy == null) {
            this.createdBy = 1;  // Asigna el valor del ID de un usuario predeterminado (ejemplo 1).
        }
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }


}
