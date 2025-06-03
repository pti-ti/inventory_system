package com.pti_sa.inventory_system.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceResponseDTO {

    private Integer id;
    private String code;
    private String brandName;
    private String modelName;
    private String userEmail;
    private String serial;
    private String type;
    private String note;
    private String specification;
    private String locationName;
    private String statusName;
    private BigDecimal price;
    private String createdByEmail;
    private LocalDateTime createdAt;

}
