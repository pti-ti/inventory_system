package com.pti_sa.inventory_system.infrastructure.mapper;

import com.pti_sa.inventory_system.domain.model.Location;
import com.pti_sa.inventory_system.infrastructure.entity.LocationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    Location toModel(LocationEntity entity);
    LocationEntity toEntity(Location model);
}
