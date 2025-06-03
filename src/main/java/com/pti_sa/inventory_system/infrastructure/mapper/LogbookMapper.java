package com.pti_sa.inventory_system.infrastructure.mapper;

import com.pti_sa.inventory_system.application.dto.request.LogbookRequestDTO;

import com.pti_sa.inventory_system.application.dto.response.LogbookResponseDTO;
import com.pti_sa.inventory_system.domain.model.Logbook;
import com.pti_sa.inventory_system.infrastructure.entity.LogbookEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface LogbookMapper {
    Logbook toModel(LogbookEntity entity);
    LogbookEntity toEntity(Logbook logbook);

    LogbookRequestDTO toRequestDTO(Logbook logbook);
    LogbookRequestDTO toRequestDTO(LogbookEntity logbookEntity);

    @Mapping(target = "deviceCode", source = "device.code")
    @Mapping(target = "deviceBrand", source = "brand.name")
    @Mapping(target = "deviceModel", source = "model.name")
    @Mapping(target = "deviceSerial", source = "device.serial")
    @Mapping(target = "deviceStatus", source = "status.name")
    @Mapping(target = "deviceLocation", source = "location.name")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "changes", source = "changes")
    @Mapping(target = "updatedByEmail", ignore = true)
    LogbookResponseDTO toResponseDTO(Logbook logbook);

}
