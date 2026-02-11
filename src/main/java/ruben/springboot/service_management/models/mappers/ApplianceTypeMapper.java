package ruben.springboot.service_management.models.mappers;

import ruben.springboot.service_management.models.ApplianceType;
import ruben.springboot.service_management.models.dtos.ApplianceTypeDto;

public class ApplianceTypeMapper {

    public static ApplianceType toEntity(ApplianceTypeDto dto) {
        ApplianceType at = new ApplianceType();
        at.setName(dto.name);
        return at;
    }

    public static ApplianceType update(ApplianceTypeDto dto, ApplianceType at) {
        at.setName(dto.name);
        return at;
    }

    public static ApplianceTypeDto toDto(ApplianceType at) {
        ApplianceTypeDto dto = new ApplianceTypeDto();
        dto.id = at.getId();
        dto.name = at.getName();
        return dto;
    }

}
