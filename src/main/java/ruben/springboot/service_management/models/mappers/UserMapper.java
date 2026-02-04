package ruben.springboot.service_management.models.mappers;

import ruben.springboot.service_management.models.User;
import ruben.springboot.service_management.models.dto.UserResponseDto;

public class UserMapper {
    
    public static UserResponseDto toResponse(User u) {
        UserResponseDto dto = new UserResponseDto();
        dto.id = u.getId();
        dto.name = u.getName();
        dto.phone = u.getPhone();
        dto.username = u.getUsername();
        dto.role = u.getRole().getLabelEs();
        dto.active = u.isActive();
        dto.createdAt = u.getCreatedAt();
        dto.lastUpdatedAt = u.getLastUpdatedAt();
        return dto;
    }
    
}
