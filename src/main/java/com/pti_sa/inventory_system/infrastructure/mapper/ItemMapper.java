package com.pti_sa.inventory_system.infrastructure.mapper;

import com.pti_sa.inventory_system.application.dto.response.ItemResponseDTO;
import com.pti_sa.inventory_system.domain.model.Item;
import com.pti_sa.inventory_system.infrastructure.entity.ItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    Item toModel(ItemEntity entity);
    ItemEntity toEntity(Item model);
    List<Item> toModelList(List<ItemEntity> entities);
    List<ItemEntity> toEntityList(List<Item> models);

    @Named("toItemResponseList") // Mapeo completo con id y nombre
    default List<ItemResponseDTO> toItemResponseList(List<ItemEntity> items) {
        if (items == null) {
            return null;
        }
        return items.stream()
                .map(item -> new ItemResponseDTO(item.getId(), item.getName()))
                .toList();
    }

}


