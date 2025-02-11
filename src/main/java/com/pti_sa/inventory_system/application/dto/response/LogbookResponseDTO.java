package com.pti_sa.inventory_system.application.dto.response;

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
    private String deviceName;
    private String userEmail;
    private String statusName;
    private String locationName;
    private String note;
    private LocalDateTime createdAt;
}
