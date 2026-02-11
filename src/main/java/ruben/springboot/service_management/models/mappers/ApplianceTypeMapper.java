package ruben.springboot.service_management.models.mappers;

import ruben.springboot.service_management.models.ApplianceType;
import ruben.springboot.service_management.models.dtos.requests.ApplianceTypeRequestDto;

public class ApplianceTypeMapper {

    public static ApplianceType toEntity(ApplianceTypeRequestDto dto) {
        ApplianceType t = new ApplianceType();
        t.setName(dto.name);
        return t;
    }
    
}
