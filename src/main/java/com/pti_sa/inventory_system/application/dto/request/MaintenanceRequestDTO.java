package com.pti_sa.inventory_system.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceRequestDTO {

    private Integer id;
    private DeviceRequestDTO device;
    private UserRequestDTO user;
    private String maintenanceType;
    private LocalDate maintenanceDate;
    private String comment;
    private Integer updatedBy;
}
