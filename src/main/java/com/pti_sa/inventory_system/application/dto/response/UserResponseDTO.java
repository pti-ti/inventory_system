package com.pti_sa.inventory_system.application.dto.response;

import com.pti_sa.inventory_system.domain.model.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private String email;
    // private UserType userType; Muestra el tipo de usuario
    private LocationResponseDTO location;

}
