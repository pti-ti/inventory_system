package com.pti_sa.inventory_system.domain.port;

import javax.management.relation.Role;
import java.util.List;
import java.util.Optional;

public interface IRoleRepository {
    Role save(Role role);
    Optional<Role> findById(Long id);
    List<Role> findAll();
    void deleteById(Long id);
}
