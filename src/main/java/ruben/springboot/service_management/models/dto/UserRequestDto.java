package ruben.springboot.service_management.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ruben.springboot.service_management.models.enums.UserRoles;

public class UserRequestDto {

    @NotBlank(message = "name is required")
    @Size(max = 45, message = "name must be at most 45 characters")
    public String name;

    @NotBlank(message = "phone is required")
    @Size(min = 9, max = 12, message = "phone must be between 9 and 12 characters")
    public String phone;

    @NotBlank(message = "username is required")
    @Size(min = 3, max = 20, message = "username must be between 3 and 20 characters")
    public String username;

    @NotBlank(message = "password is required")
    @Size(min = 6, message = "password must be at least 6 characters")
    public String password;

    @NotNull(message = "role is required")
    public UserRoles role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRoles getRole() {
        return role;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }
    
}
