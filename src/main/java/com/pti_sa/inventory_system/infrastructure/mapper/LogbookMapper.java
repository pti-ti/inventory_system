package com.pti_sa.inventory_system.infrastructure.mapper;

import com.pti_sa.inventory_system.domain.model.Logbook;
import com.pti_sa.inventory_system.infrastructure.entity.LogbookEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LogbookMapper {
    LogbookMapper INSTANCE = Mappers.getMapper(LogbookMapper.class);

    Logbook toModel(LogbookEntity entity);

    LogbookEntity toEntity(Logbook model);
}
