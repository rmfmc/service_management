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
import ruben.springboot.service_management.repositories.ApplianceTypeRepository;
import ruben.springboot.service_management.repositories.BrandRepository;

@Component
public class ApplianceMapper {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ApplianceTypeRepository applianceTypeRepository;

    @Autowired
    private BrandRepository brandRepository;

    public Appliance toEntity(ApplianceRequestDto dto, Long addressId) {
        Appliance a = new Appliance();

        a.setAddress(addressRepository.findById(addressId)
                .orElseThrow(() -> new NotFoundException("Address not found")));

        a.setApplianceType(applianceTypeRepository.findById(dto.applianceTypeId)
                .orElseThrow(() -> new NotFoundException("ApplianceType not found")));

        if (dto.brandId != null) {
            a.setBrand(brandRepository.findById(dto.brandId)
                    .orElseThrow(() -> new NotFoundException("Brand not found")));
        } else {
            a.setBrand(null);
        }

        a.setModel(dto.model == null ? null : dto.model.trim());
        a.setSerialNumber(dto.serialNumber == null ? null : dto.serialNumber.trim());
        a.setActive(dto.active == null ? true : dto.active);

        return a;
    }

    public Appliance update(Appliance appliance, ApplianceRequestDto dto) {

        appliance.setApplianceType(applianceTypeRepository.findById(dto.applianceTypeId)
                .orElseThrow(() -> new NotFoundException("ApplianceType not found: " + dto.applianceTypeId)));

        if (dto.brandId != null) {
            appliance.setBrand(brandRepository.findById(dto.brandId)
                    .orElseThrow(() -> new NotFoundException("Brand not found: " + dto.brandId)));
        } else {
            appliance.setBrand(null);
        }

        appliance.setModel(dto.model == null ? null : dto.model.trim());
        appliance.setSerialNumber(dto.serialNumber == null ? null : dto.serialNumber.trim());
        appliance.setActive(dto.active == null ? true : dto.active);

        return appliance;
    }

    public ApplianceResponseDto toResponse(Appliance a) {
        ApplianceResponseDto dto = new ApplianceResponseDto();
        dto.id = a.getId();

        dto.clientId = a.getAddress().getClient().getId();
        dto.clientName = a.getAddress().getClient().getName();
        dto.addressId = a.getAddress().getId();
        dto.addressName = a.getAddress().getAddress();

        dto.applianceTypeId = a.getApplianceType().getId();
        dto.applianceTypeName = a.getApplianceType().getName();
        dto.brandId = a.getBrand() != null ? a.getBrand().getId() : null;
        dto.brandName = a.getBrand() != null ? a.getBrand().getName() : null;
        dto.model = a.getModel();
        dto.serialNumber = a.getSerialNumber();
        dto.active = a.isActive();
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

    public ApplianceListDto toList(Appliance a) {

        ApplianceListDto dto = new ApplianceListDto();

        dto.id = a.getId();

        dto.type = a.getApplianceType().getName();
        dto.brand = a.getBrand() != null ? a.getBrand().getName() : null;
        dto.model = a.getModel();
        dto.serialNumber = a.getSerialNumber();
        dto.active = a.isActive();

        return dto;
    }

}
