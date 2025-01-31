package com.pti_sa.inventory_system.infrastructure.mapper;

import com.pti_sa.inventory_system.infrastructure.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import javax.management.relation.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    @Mapping(source = "idRol", target = "id")
    @Mapping(source = "nameRol", target = "name")
    Role toDomain(RoleEntity entity);

    @Mapping(source = "id", target = "idRol")
    @Mapping(source = "name", target = "nameRol")
    RoleEntity toEntity(Role domain);
}
