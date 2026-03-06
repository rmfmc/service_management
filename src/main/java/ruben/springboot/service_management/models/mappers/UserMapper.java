package ruben.springboot.service_management.models.mappers;

import ruben.springboot.service_management.models.User;
import ruben.springboot.service_management.models.dtos.requests.UserRequestDto;
import ruben.springboot.service_management.models.dtos.requests.UserWithoutPasswordRequestDto;
import ruben.springboot.service_management.models.dtos.responses.UserResponseDto;

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

    public static User toCreateEntityExceptPassword(UserRequestDto dto) {
        User u = new User();
        u.setName(dto.name.trim());
        u.setPhone(dto.phone.trim());
        u.setUsername(dto.username.trim().toLowerCase());
        u.setRole(dto.role);
        u.setActive(true);
        return u;
    }

    public static User updateEntityExceptPassword(User userDB, UserWithoutPasswordRequestDto dto) {
        userDB.setName(dto.name.trim());
        userDB.setPhone(dto.phone.trim());
        userDB.setUsername(dto.username.trim().toLowerCase());
        userDB.setRole(dto.role);
        userDB.setActive(dto.active);
        return userDB;
    }
}
