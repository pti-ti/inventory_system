package com.pti_sa.inventory_system.infrastructure.mapper;

import com.pti_sa.inventory_system.application.dto.response.MaintenanceResponseDTO;
import com.pti_sa.inventory_system.domain.model.Maintenance;
import com.pti_sa.inventory_system.infrastructure.entity.MaintenanceEntity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {ItemMapper.class})
public interface MaintenanceMapper {

    Maintenance toModel(MaintenanceEntity entity);
    MaintenanceEntity toEntity(Maintenance maintenance);

    @Mapping(source = "device.id", target = "deviceId")
    @Mapping(source = "device.code", target = "deviceCode")
    @Mapping(source = "device.name", target = "deviceName")
    @Mapping(source = "user.email", target = "userEmail")
    @Mapping(source = "user.location.name", target = "userLocation")
    @Mapping(source = "items", target = "items", qualifiedByName = "toItemResponseList")
    MaintenanceResponseDTO toDto(Maintenance maintenance);
}

