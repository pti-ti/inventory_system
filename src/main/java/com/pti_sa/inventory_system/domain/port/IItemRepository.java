package com.pti_sa.inventory_system.domain.port;

import com.pti_sa.inventory_system.domain.model.Item;

import java.util.List;
import java.util.Optional;

public interface IItemRepository {
    Item save(Item item);
    Optional<Item> findById(Integer id);
    List<Item> findAll();
    void deleteById(Integer id);
    boolean existsById(Integer id);
}