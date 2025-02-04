package com.pti_sa.inventory_system.infrastructure.mapper;

import com.pti_sa.inventory_system.domain.model.User;
import com.pti_sa.inventory_system.infrastructure.entity.UserEntity;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {

    User toModel(UserEntity entity);
    UserEntity toEntity(User model);


}
