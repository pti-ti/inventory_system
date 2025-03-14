package com.pti_sa.inventory_system.infrastructure.mapper;

import com.pti_sa.inventory_system.domain.model.Item;
import com.pti_sa.inventory_system.infrastructure.entity.ItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemEntity toEntity(Item item);

    Item toModel(ItemEntity itemEntity);

    List<ItemEntity> toEntityList(List<Item> items);

    List<Item> toModelList(List<ItemEntity> itemEntities);
}
