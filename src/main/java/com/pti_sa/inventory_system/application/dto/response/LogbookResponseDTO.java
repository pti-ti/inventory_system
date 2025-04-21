package com.pti_sa.inventory_system.application.dto.response;

import com.pti_sa.inventory_system.domain.model.Brand;
import com.pti_sa.inventory_system.domain.model.Device;
import com.pti_sa.inventory_system.domain.model.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogbookResponseDTO {
    private Integer id;
    private String deviceCode;
    private String deviceBrand;
    private String deviceModel;
    private String deviceStatus;
    private String deviceLocation;
    private String deviceSerial;
    private String userEmail;
    //private String type;
    private String note;
    private LocalDateTime createdAt;
}
