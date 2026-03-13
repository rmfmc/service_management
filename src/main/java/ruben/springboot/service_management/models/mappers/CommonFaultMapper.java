package ruben.springboot.service_management.models.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ruben.springboot.service_management.errors.NotFoundException;
import ruben.springboot.service_management.models.CommonFault;
import ruben.springboot.service_management.models.dtos.CommonFaultDto;
import ruben.springboot.service_management.repositories.ApplianceTypeRepository;
import ruben.springboot.service_management.repositories.CommonFaultRepository;

@Component
public class CommonFaultMapper {

    @Autowired
    private ApplianceTypeRepository applianceTypeRepository;
    @Autowired
    private CommonFaultRepository repository;

    public CommonFault toEntity(CommonFaultDto dto) {
        CommonFault cf = new CommonFault();
        cf.setApplianceType(applianceTypeRepository.findById(dto.applianceTypeId)
                .orElseThrow(() -> new NotFoundException("Tipo de electrodoméstico", dto.applianceTypeId)));
        cf.setName(dto.name);
        return cf;
    }

    public CommonFault update(CommonFaultDto dto, Long commonFaultId) {
        CommonFault cf = repository.findById(commonFaultId).orElseThrow(() -> new NotFoundException("Fallo común", commonFaultId));
        cf.setApplianceType(applianceTypeRepository.findById(dto.applianceTypeId)
                .orElseThrow(() -> new NotFoundException("Tipo de electrodoméstico", dto.applianceTypeId)));
        cf.setName(dto.name);
        return cf;
    }

    public CommonFaultDto toDto(CommonFault cf) {
        CommonFaultDto dto = new CommonFaultDto();
        dto.id = cf.getId();
        dto.applianceTypeId = cf.getApplianceType().getId();
        dto.name = cf.getName();
        return dto;
    }

}
