package ruben.springboot.service_management.models.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ruben.springboot.service_management.errors.NotFoundException;
import ruben.springboot.service_management.models.Appliance;
import ruben.springboot.service_management.models.dtos.lists.ApplianceListDto;
import ruben.springboot.service_management.models.dtos.requests.ApplianceRequestDto;
import ruben.springboot.service_management.models.dtos.responses.ApplianceOnlyResponseDto;
import ruben.springboot.service_management.models.dtos.responses.ApplianceResponseDto;
import ruben.springboot.service_management.repositories.AddressRepository;
import ruben.springboot.service_management.repositories.ApplianceRepository;
import ruben.springboot.service_management.repositories.ApplianceTypeRepository;
import ruben.springboot.service_management.repositories.BrandRepository;

@Component
public class ApplianceMapper {

    @Autowired
    private ApplianceRepository repository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ApplianceTypeRepository applianceTypeRepository;

    @Autowired
    private BrandRepository brandRepository;

    public Appliance toEntity(ApplianceRequestDto dto, Long addressId) {
        
        Appliance a = new Appliance();
        resolve(a, addressId, dto);

        return a;
    }

    public Appliance update(Long applianceId, Long addressId, ApplianceRequestDto dto) {

        Appliance applianceDB = repository.findById(applianceId).orElseThrow(() -> new NotFoundException("Electrodoméstico" + dto.applianceTypeId));
        resolve(applianceDB, addressId, dto);

        return applianceDB;
    }

    public ApplianceResponseDto toResponse(Appliance a) {
        ApplianceResponseDto dto = new ApplianceResponseDto();

        dto.id = a.getId();
        dto.applianceTypeName = a.getApplianceType().getName();
        dto.brandName = a.getBrand() != null ? a.getBrand().getName() : null;
        dto.model = a.getModel();
        dto.serialNumber = a.getSerialNumber();
        dto.active = a.isActive();
        dto.address = AddressMapper.toList(a.getAddress());
        dto.client = ClientMapper.toList(a.getAddress().getClient());
        return dto;

    }

    public static ApplianceOnlyResponseDto toOnlyResponse(Appliance a) {
        ApplianceOnlyResponseDto dto = new ApplianceOnlyResponseDto();

        dto.id = a.getId();
        dto.applianceTypeName = a.getApplianceType().getName();
        dto.brandId = a.getBrand() != null ? a.getBrand().getId() : null;
        dto.brandName = a.getBrand() != null ? a.getBrand().getName() : null;
        dto.model = a.getModel();
        dto.serialNumber = a.getSerialNumber();
        dto.active = a.isActive();
        return dto;

    }

    public static ApplianceListDto toList(Appliance a) {

        ApplianceListDto dto = new ApplianceListDto();

        dto.id = a.getId();
        dto.type = a.getApplianceType().getName();
        dto.brand = a.getBrand() != null ? a.getBrand().getName() : null;
        dto.model = a.getModel();
        dto.serialNumber = a.getSerialNumber();
        dto.active = a.isActive();

        return dto;
    }

    private Appliance resolve(Appliance a, Long addressId, ApplianceRequestDto dto) {

        a.setAddress(addressRepository.findById(addressId)
                .orElseThrow(() -> new NotFoundException("Dirección", addressId)));

        a.setApplianceType(applianceTypeRepository.findById(dto.applianceTypeId)
                .orElseThrow(() -> new NotFoundException("Tipo de electrodoméstico", dto.applianceTypeId)));

        if (dto.brandId != null) {
            a.setBrand(brandRepository.findById(dto.brandId)
                    .orElseThrow(() -> new NotFoundException("Marca", dto.brandId)));
        } else {
            a.setBrand(null);
        }

        a.setModel(dto.model == null ? null : dto.model.trim());
        a.setSerialNumber(dto.serialNumber == null ? null : dto.serialNumber.trim());
        a.setActive(dto.active == null ? true : dto.active);

        return a;
    }

}
