package com.pti_sa.inventory_system.infrastructure.mapper;

import com.pti_sa.inventory_system.domain.model.User;
import com.pti_sa.inventory_system.infrastructure.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toModel(UserEntity entity);
    UserEntity toEntity(User model);


}
