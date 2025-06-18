package com.pti_sa.inventory_system.application.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LastDeviceActionResponseDTO {

    private String email;
    private String action;
    private String deviceCode;
    private OffsetDateTime date;
}
