package com.pti_sa.inventory_system.application;

import com.pti_sa.inventory_system.application.dto.response.ItemResponseDTO;
import com.pti_sa.inventory_system.domain.model.Item;
import com.pti_sa.inventory_system.domain.port.IItemRepository;
import com.pti_sa.inventory_system.infrastructure.mapper.ItemMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final IItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public ItemService(IItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    /*public Item saveItem(Item item) {
        return itemRepository.save(item);
    }*/

    public ItemResponseDTO saveItem(Item item) {
        var entity = itemMapper.toEntity(item);
        var savedEntity = itemRepository.save(item);

        return new ItemResponseDTO(savedEntity.getId(), savedEntity.getName());
    }

    public Optional<Item> getItemById(Integer id) {
        return itemRepository.findById(id);
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Optional<Item> updateItem(Integer id, Item updatedItem) {
        return itemRepository.findById(id).map(existingItem -> {
            existingItem.setName(updatedItem.getName());
            return itemRepository.save(existingItem);
        });
    }

    public boolean deleteItem(Integer id) {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
