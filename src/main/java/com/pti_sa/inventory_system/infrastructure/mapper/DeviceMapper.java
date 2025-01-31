package com.pti_sa.inventory_system.infrastructure.mapper;

import com.pti_sa.inventory_system.domain.model.Device;
import com.pti_sa.inventory_system.infrastructure.entity.DeviceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DeviceMapper {
    DeviceMapper INSTANCE = Mappers.getMapper(DeviceMapper.class);

    @Mapping(source = "idDevice", target = "id")
    @Mapping(source = "codDevice", target= "code")
    @Mapping(source = "nameDevice", target= "name")
    Device toDomain(DeviceEntity entity);

    @Mapping(source = "id", target = "idDevice")
    @Mapping(source = "code", target = "codDevice")
    @Mapping(source = "name", target = "nameDevice")
    DeviceEntity toEntity(Device domain);
}
