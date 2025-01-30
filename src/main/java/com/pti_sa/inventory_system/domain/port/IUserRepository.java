package com.pti_sa.inventory_system.domain.port;

import com.pti_sa.inventory_system.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {

    User save(User user);
    Optional<User> findById(Long id);
    List<User> findAll();
    void deleteById(Long id);
}
