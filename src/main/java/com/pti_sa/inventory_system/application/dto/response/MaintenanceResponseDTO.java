package com.pti_sa.inventory_system.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceResponseDTO {
    private Integer id;
    private Integer deviceId;
    private String deviceCode;
    private String deviceBrand;
    private String deviceModel;
    private String userEmail;
    private String userLocation;
    private String maintenanceType;
    private LocalDate maintenanceDate;
    private String comment;
    //private List<String> itemIds;
    private List<ItemResponseDTO> items;
    private Integer createdBy;
    private String createdByEmail;
}
