package com.pti_sa.inventory_system.domain.port;

import com.pti_sa.inventory_system.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {

    User save(User user);
    User update(User user);
    Optional<User> findById(Integer id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    void deleteById(Integer id);
    boolean existsByEmail(String email);
}
