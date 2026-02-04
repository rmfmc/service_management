package ruben.springboot.service_management.models.dto;

import java.time.LocalDateTime;

import ruben.springboot.service_management.models.UserRoles;

public class UserResponseDto {

    public Long id;
    public String name;
    public String phone;
    public String username;
    public UserRoles role;
    public boolean active;
    public LocalDateTime createdAt;
    public LocalDateTime lastUpdatedAt;

}
