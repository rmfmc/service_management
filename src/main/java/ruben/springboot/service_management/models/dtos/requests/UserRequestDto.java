package ruben.springboot.service_management.models.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ruben.springboot.service_management.models.enums.UserRole;

public class UserRequestDto {

    @NotBlank(message = "el nombre es obligatorio")
    @Size(max = 45, message = "el nombre debe tener como máximo 45 caracteres")
    public String name;

    @NotBlank(message = "el teléfono es obligatorio")
    @Size(min = 9, max = 12, message = "el teléfono debe tener entre 9 y 12 caracteres")
    public String phone;

    @NotBlank(message = "el nombre de usuario es obligatorio")
    @Size(min = 3, max = 20, message = "el nombre de usuario debe tener entre 3 y 20 caracteres")
    public String username;

    @NotBlank(message = "la contraseña es obligatoria")
    @Size(min = 6, message = "la contraseña debe tener al menos 6 caracteres")
    public String password;

    @NotNull(message = "el rol es obligatorio")
    public UserRole role;

    
}
