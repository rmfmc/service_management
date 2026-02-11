package ruben.springboot.service_management.models.dtos.responses;

import java.time.LocalDateTime;

public class UserResponseDto {

    public Long id;
    public String name;
    public String phone;
    public String username;
    public String role;
    public boolean active;
    public LocalDateTime createdAt;
    public LocalDateTime lastUpdatedAt;

}
