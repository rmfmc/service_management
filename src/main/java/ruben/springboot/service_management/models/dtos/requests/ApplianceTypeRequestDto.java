package ruben.springboot.service_management.models.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ApplianceTypeRequestDto {

    @NotBlank
    @Size(max = 45)
    public String name;
    
}
