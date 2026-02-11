package ruben.springboot.service_management.models.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ruben.springboot.service_management.errors.NotFoundException;
import ruben.springboot.service_management.models.CommonFault;
import ruben.springboot.service_management.models.dtos.requests.CommonFaultRequestDto;
import ruben.springboot.service_management.repositories.ApplianceTypeRepository;

@Component
public class CommonFaultMapper {

    @Autowired
    private ApplianceTypeRepository applianceTypeRepository;


    public CommonFault toEntity(CommonFaultRequestDto dto) {
        CommonFault f = new CommonFault();
        f.setApplianceType(applianceTypeRepository.findById(dto.applianceTypeId)
                .orElseThrow(() -> new NotFoundException("applianceType not found")));
        f.setName(dto.name);
        return f;
    }

}
