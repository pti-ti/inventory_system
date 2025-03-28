package com.pti_sa.inventory_system.infrastructure.mapper;

import com.pti_sa.inventory_system.application.dto.response.ModelResponseDTO;
import com.pti_sa.inventory_system.domain.model.Model;
import com.pti_sa.inventory_system.infrastructure.entity.ModelEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ModelMapper {

    Model toModel(ModelEntity modelEntity);
    ModelEntity toEntity(Model model);
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "createdAt", target = "createdAt")
    ModelResponseDTO toDTO(Model model);

}
