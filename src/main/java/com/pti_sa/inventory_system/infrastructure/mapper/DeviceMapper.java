package com.pti_sa.inventory_system.infrastructure.mapper;

import com.pti_sa.inventory_system.application.dto.request.DeviceRequestDTO;
import com.pti_sa.inventory_system.application.dto.response.DeviceResponseDTO;
import com.pti_sa.inventory_system.domain.model.Device;
import com.pti_sa.inventory_system.infrastructure.entity.DeviceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface DeviceMapper {
    Device toModel(DeviceEntity entity);
    DeviceEntity toEntity(Device device);

    DeviceRequestDTO toRequestDTO(Device device);
    DeviceRequestDTO toRequestDTO(DeviceEntity deviceEntity);

    @Mapping(target = "brandName", source = "device.brand.name")
    @Mapping(target = "modelName", source = "device.model.name")
    @Mapping(target = "locationName", source = "device.location.name")
    @Mapping(target = "statusName", source = "device.status.name")
    DeviceResponseDTO toResponseDTO(Device device);
}
