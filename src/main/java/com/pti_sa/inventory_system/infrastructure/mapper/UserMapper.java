package com.pti_sa.inventory_system.infrastructure.mapper;

import com.pti_sa.inventory_system.application.dto.UserResponseDTO;
import com.pti_sa.inventory_system.domain.model.User;
import com.pti_sa.inventory_system.infrastructure.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = LocationMapper.class)
public interface UserMapper {

    User toModel(UserEntity entity);
    UserEntity toEntity(User model);

    @Mapping(target = "email", source = "email")
    @Mapping(target = "location", source = "location")
    UserResponseDTO toResponseDTO(User user);


}
