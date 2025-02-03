package com.pti_sa.inventory_system.infrastructure.adapter;

import com.pti_sa.inventory_system.infrastructure.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserJpaRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
