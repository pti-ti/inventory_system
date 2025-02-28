package com.pti_sa.inventory_system.domain.port;

import com.pti_sa.inventory_system.domain.model.Device;
import com.pti_sa.inventory_system.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {

    User save(User user);
    User update(User user);
    Optional<User> findById(Integer id);
    Optional<User> findByEmailLogin(String email);
    List<User> findByEmail(String email);
    List<User> findAll();
    List<User> findAllByDeletedFalse();

    void deleteById(Integer id);
    boolean existsByEmail(String email);
    List<User> findByLocationId(Integer locationId);
}
