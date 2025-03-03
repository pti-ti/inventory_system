package com.pti_sa.inventory_system.application.dto.response;

import com.pti_sa.inventory_system.domain.model.Status;
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
    private StatusResponseDTO status;
    private BigDecimal price;


}
