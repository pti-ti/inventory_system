package com.pti_sa.inventory_system.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Logbook {
    private Long id;
    private Long deviceId;
    private Long userId;
    private Long statusId;
    private Long locationId;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;
}
