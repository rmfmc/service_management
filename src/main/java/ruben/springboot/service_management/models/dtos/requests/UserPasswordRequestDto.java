package ruben.springboot.service_management.models.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserPasswordRequestDto {

    @NotBlank(message = "la contraseña es obligatoria")
    @Size(min = 6, message = "la contraseña debe tener al menos 6 caracteres")
    public String password;
    
}
