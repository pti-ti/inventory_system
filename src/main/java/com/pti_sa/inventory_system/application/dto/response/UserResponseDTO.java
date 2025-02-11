package com.pti_sa.inventory_system.application.dto.response;

import com.pti_sa.inventory_system.domain.model.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private String email;
    // private UserType userType; Muestra el tipo de usuario
    private LocationResponseDTO location;
    private List<DeviceResponseDTO> devices;

}
