package com.pti_sa.inventory_system.infrastructure.mapper;

import com.pti_sa.inventory_system.application.dto.response.MaintenanceResponseDTO;
import com.pti_sa.inventory_system.domain.model.Maintenance;
import com.pti_sa.inventory_system.infrastructure.adapter.IUserJpaRepository;
import com.pti_sa.inventory_system.infrastructure.entity.MaintenanceEntity;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {ItemMapper.class})
public abstract class MaintenanceMapper {

    @Autowired
    protected IUserJpaRepository iUserJpaRepository;

    public abstract Maintenance toModel(MaintenanceEntity entity);
    public abstract MaintenanceEntity toEntity(Maintenance maintenance);

    @Mapping(source = "device.id", target = "deviceId")
    @Mapping(source = "device.code", target = "deviceCode")
    @Mapping(source = "device.name", target = "deviceName")
    @Mapping(source = "user.email", target = "userEmail")
    @Mapping(source = "user.location.name", target = "userLocation")
    @Mapping(source = "items", target = "items", qualifiedByName = "toItemResponseList")
    @Mapping(source = "createdBy", target = "createdByEmail", qualifiedByName = "mapCreatedByEmail")
    public abstract MaintenanceResponseDTO toDto(Maintenance maintenance);

    @Named("mapCreatedByEmail")
    public String mapCreatedByEmail(Integer createdBy) {
        return createdBy != null ? iUserJpaRepository.findById(createdBy).map(user -> user.getEmail()).orElse(null) : null;
    }
}
