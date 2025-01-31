package com.pti_sa.inventory_system.infrastructure.mapper;

import com.pti_sa.inventory_system.domain.model.Status;
import com.pti_sa.inventory_system.infrastructure.entity.StatusEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StatusMapper {
    StatusMapper INSTANCE = Mappers.getMapper(StatusMapper.class);

    @Mapping(source = "idStatus", target = "id")
    @Mapping(source = "statusName", target = "name")
    Status toDomain(StatusEntity entity);

    @Mapping(source = "id", target = "idStatus")
    @Mapping(source = "name", target = "statusName")
    StatusEntity toEntity(Status domain);
}
