package ruben.springboot.service_management.models.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ruben.springboot.service_management.models.enums.UserRole;

public class UserWithoutPasswordRequestDto {

    @NotBlank(message = "name is required")
    @Size(max = 45, message = "name must be at most 45 characters")
    public String name;

    @NotBlank(message = "phone is required")
    @Size(min = 9, max = 12, message = "phone must be between 9 and 12 characters")
    public String phone;

    @NotBlank(message = "username is required")
    @Size(min = 3, max = 20, message = "username must be between 3 and 20 characters")
    public String username;

    @NotNull(message = "active is required")
    public boolean active;

    @NotNull(message = "role is required")
    public UserRole role;

    
}
