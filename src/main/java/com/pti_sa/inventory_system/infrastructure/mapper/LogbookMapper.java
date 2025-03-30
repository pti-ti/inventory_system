package com.pti_sa.inventory_system.infrastructure.mapper;

import com.pti_sa.inventory_system.application.dto.response.LogbookResponseDTO;
import com.pti_sa.inventory_system.domain.model.Logbook;
import com.pti_sa.inventory_system.infrastructure.entity.LogbookEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LogbookMapper {

    Logbook toModel(LogbookEntity entity);

    LogbookEntity toEntity(Logbook logbook);

    @Mapping(target = "deviceCode", source = "device.code")
    @Mapping(target = "deviceBrand", source = "device.brand.name")
    @Mapping(target = "deviceModel", source = "device.model.name")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "statusName", source = "status.name")
    @Mapping(target = "locationName", source = "location.name")
    LogbookResponseDTO toResponseDTO(Logbook logbook);
}
