package com.pti_sa.inventory_system.infrastructure.mapper;

import com.pti_sa.inventory_system.domain.model.Maintenance;
import com.pti_sa.inventory_system.infrastructure.entity.MaintenanceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MaintenanceMapper {

    Maintenance toModel(MaintenanceEntity entity);
    MaintenanceEntity toEntity(Maintenance model);
}
