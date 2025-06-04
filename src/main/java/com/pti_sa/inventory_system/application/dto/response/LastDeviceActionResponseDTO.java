package com.pti_sa.inventory_system.application.dto.response;

import com.pti_sa.inventory_system.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LastDeviceActionResponseDTO {

    private String email;
    private String action;
    private String deviceCode;
    private LocalDateTime date;
}
