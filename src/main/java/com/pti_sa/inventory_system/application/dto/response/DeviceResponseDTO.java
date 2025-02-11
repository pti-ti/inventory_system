package com.pti_sa.inventory_system.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceResponseDTO {

    private String code;
    private String serial;
    private String type;
    private String specification;
    private String status;


}
