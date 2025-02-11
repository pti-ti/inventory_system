package com.pti_sa.inventory_system.infrastructure.mapper;

import com.pti_sa.inventory_system.application.dto.response.DeviceResponseDTO;
import com.pti_sa.inventory_system.domain.model.Device;
import com.pti_sa.inventory_system.infrastructure.entity.DeviceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface DeviceMapper {
    Device toModel(DeviceEntity entity);
    DeviceEntity toEntity(Device device);

    @Mapping(target = "status", source = "status.name")
    DeviceResponseDTO toResponseDTO(Device device);
}
