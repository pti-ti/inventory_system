package com.pti_sa.inventory_system.infrastructure.mapper;

import com.pti_sa.inventory_system.application.dto.response.UserResponseDTO;
import com.pti_sa.inventory_system.domain.model.User;
import com.pti_sa.inventory_system.infrastructure.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = LocationMapper.class)
public interface UserMapper {

    User toModel(UserEntity entity);
    UserEntity toEntity(User model);

    @Mapping(target = "location", source = "location")
    //@Mapping(target = "userType", source = "userType") Muestra el tipo de usuario
    UserResponseDTO toResponseDTO(User user);


}
