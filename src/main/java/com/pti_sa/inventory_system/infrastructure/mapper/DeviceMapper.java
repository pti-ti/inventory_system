package com.pti_sa.inventory_system.infrastructure.mapper;

import com.pti_sa.inventory_system.domain.model.Device;
import com.pti_sa.inventory_system.infrastructure.entity.DeviceEntity;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface DeviceMapper {
    Device toModel(DeviceEntity entity);
    DeviceEntity toEntity(Device model);
}
