package com.pti_sa.inventory_system.infrastructure.mapper;

import com.pti_sa.inventory_system.domain.model.Maintenance;
import com.pti_sa.inventory_system.infrastructure.entity.MaintenanceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MaintenanceMapper {
    MaintenanceMapper INSTANCE = Mappers.getMapper(MaintenanceMapper.class);

    @Mapping(source = "idMaintenance", target = "id")
    @Mapping(source = "idDevice", target = "deviceId")
    @Mapping(source = "idUser", target = "userId")
    Maintenance toDomain(MaintenanceEntity entity);

    @Mapping(source = "id", target = "idMaintenance")
    @Mapping(source = "deviceId", target = "idDevice")
    @Mapping(source = "userId", target = "idUser")
    MaintenanceEntity toEntity(Maintenance domain);
}
