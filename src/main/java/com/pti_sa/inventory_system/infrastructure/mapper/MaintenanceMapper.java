package com.pti_sa.inventory_system.infrastructure.mapper;

import com.pti_sa.inventory_system.application.dto.response.MaintenanceResponseDTO;
import com.pti_sa.inventory_system.domain.model.Maintenance;
import com.pti_sa.inventory_system.infrastructure.entity.MaintenanceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MaintenanceMapper {

    Maintenance toModel(MaintenanceEntity entity);
    MaintenanceEntity toEntity(Maintenance maintenance);

    @Mapping(source = "device.id", target = "deviceId")
    @Mapping(source = "device.code", target = "deviceCode")
    @Mapping(source = "device.name", target = "deviceName")
    @Mapping(source = "user.email", target = "userEmail")
    MaintenanceResponseDTO toDto(Maintenance maintenance);
}
