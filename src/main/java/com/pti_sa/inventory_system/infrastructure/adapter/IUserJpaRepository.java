package com.pti_sa.inventory_system.infrastructure.adapter;

import com.pti_sa.inventory_system.domain.model.User;
import com.pti_sa.inventory_system.domain.model.UserType;
import com.pti_sa.inventory_system.infrastructure.entity.DeviceEntity;
import com.pti_sa.inventory_system.infrastructure.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IUserJpaRepository extends JpaRepository<UserEntity, Integer> {

    List<UserEntity> findByDeletedFalse(); // Solo usuarios activos
    Optional<UserEntity> findByIdAndDeletedFalse(Integer id); // Buscar activo por ID
    boolean existsByEmailAndDeletedFalse(String email); // Validar email solo en activos
    List<UserEntity> findByEmailContainingIgnoreCase(String email);
    boolean existsByEmail(String email);
    Optional<UserEntity> findByEmail(String email);
    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.devices WHERE u.id = :id AND u.deleted = false")
    Optional<UserEntity> findByIdWithDevices(@Param("id") Integer id);
    Optional<UserEntity> findById(Integer id);
    List<UserEntity> findByLocationId(Integer locationId);
    List<UserEntity> findByUserTypeNotAndDeletedFalse(UserType userType);

}
