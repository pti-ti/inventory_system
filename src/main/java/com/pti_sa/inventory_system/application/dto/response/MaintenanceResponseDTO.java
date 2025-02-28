package com.pti_sa.inventory_system.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceResponseDTO {
    private Integer id;
    private Integer deviceId;
    private String deviceCode;
    private String deviceName;
    private String userEmail;
    private String maintenanceType;
    private String comment;
}
