package com.pti_sa.inventory_system.infrastructure.mapper;

import com.pti_sa.inventory_system.application.dto.StatusResponseDTO;
import com.pti_sa.inventory_system.domain.model.Status;
import com.pti_sa.inventory_system.infrastructure.entity.StatusEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StatusMapper {

    Status toModel(StatusEntity entity);
    StatusEntity toEntity(Status status);

    @Mapping(target = "name", source = "name")
    StatusResponseDTO toResponseDTO(Status status);
}
