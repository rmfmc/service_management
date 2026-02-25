package ruben.springboot.service_management.models.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserPasswordRequestDto {

    @NotBlank(message = "password is required")
    @Size(min = 6, message = "password must be at least 6 characters")
    public String password;
    
}
