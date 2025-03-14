package com.pti_sa.inventory_system.infrastructure.adapter;

import com.pti_sa.inventory_system.domain.model.Item;
import com.pti_sa.inventory_system.domain.port.IItemRepository;
import com.pti_sa.inventory_system.infrastructure.entity.ItemEntity;
import com.pti_sa.inventory_system.infrastructure.mapper.ItemMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ItemJpaRepositoryImpl implements IItemRepository {

    private final IItemJpaRepository iItemJpaRepository;
    private final ItemMapper itemMapper;

    public ItemJpaRepositoryImpl(IItemJpaRepository iItemJpaRepository, ItemMapper itemMapper) {
        this.iItemJpaRepository = iItemJpaRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    public Item save(Item item) {
        ItemEntity itemEntity = itemMapper.toEntity(item);
        return itemMapper.toModel(iItemJpaRepository.save(itemEntity));
    }

    @Override
    public Optional<Item> findById(Integer id) {
        return iItemJpaRepository.findById(id).map(itemMapper::toModel);
    }

    @Override
    public List<Item> findAll() {
        return itemMapper.toModelList(iItemJpaRepository.findAll());
    }

    @Override
    public void deleteById(Integer id) {
        iItemJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return iItemJpaRepository.existsById(id);
    }
}
