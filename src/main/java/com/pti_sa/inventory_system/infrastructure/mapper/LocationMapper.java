package com.pti_sa.inventory_system.infrastructure.mapper;

import com.pti_sa.inventory_system.domain.model.Location;
import com.pti_sa.inventory_system.infrastructure.entity.LocationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    @Mapping(source = "idLocation", target = "id")
    @Mapping(source = "idLocationName", target = "name")
    Location toDomain(LocationEntity entity);

    @Mapping(source = "id", target = "idLocation")
    @Mapping(source = "name", target = "locationName")
    LocationEntity toEntity(Location domain);
}
