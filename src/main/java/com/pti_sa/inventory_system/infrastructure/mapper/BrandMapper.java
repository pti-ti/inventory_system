package com.pti_sa.inventory_system.infrastructure.mapper;

import com.pti_sa.inventory_system.application.dto.response.BrandResponseDTO;
import com.pti_sa.inventory_system.domain.model.Brand;
import com.pti_sa.inventory_system.infrastructure.entity.BrandEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    Brand toModel(BrandEntity brandEntity);
    BrandEntity toEntity(Brand brand);
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "createdAt", target = "createdAt")
    BrandResponseDTO toDTO(Brand brand);
}
