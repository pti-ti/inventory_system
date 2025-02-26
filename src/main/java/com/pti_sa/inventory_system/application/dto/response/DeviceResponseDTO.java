package com.pti_sa.inventory_system.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceResponseDTO {

    private Integer id;
    private String name;
    private String code;
    private String serial;
    private String type;
    private String specification;
    private String status;
    private BigDecimal price;


}
