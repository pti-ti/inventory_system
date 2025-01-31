package com.pti_sa.inventory_system.infrastructure.mapper;

import com.pti_sa.inventory_system.domain.model.User;
import com.pti_sa.inventory_system.infrastructure.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "idUser", target = "id")
    @Mapping(source = "idRol", target = "roledId")
    @Mapping(source = "idLocation", target = "locationId")
    User toDomain(UserEntity entity);

    @Mapping(source = "id", target = "idUser")
    @Mapping(source = "roleId", target = "idRol")
    @Mapping(source = "locationId", target = "idLocation")
    UserEntity toEntity(User domain);
}
