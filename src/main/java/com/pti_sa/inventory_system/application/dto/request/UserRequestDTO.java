package com.pti_sa.inventory_system.application.dto.request;

import com.pti_sa.inventory_system.domain.model.UserType;

import java.util.List;

public record UserRequestDTO(
        String email,
        String password,
        UserType userType,
        Integer locationId,
        List<Integer>deviceIds
) {
}
