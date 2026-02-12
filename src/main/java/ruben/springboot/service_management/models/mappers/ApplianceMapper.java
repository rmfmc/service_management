package ruben.springboot.service_management.models.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ruben.springboot.service_management.errors.NotFoundException;
import ruben.springboot.service_management.models.Appliance;
import ruben.springboot.service_management.models.Client;
import ruben.springboot.service_management.models.dtos.lists.ApplianceListDto;
import ruben.springboot.service_management.models.dtos.requests.ApplianceRequestDto;
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

    public Appliance toEntity(ApplianceRequestDto dto) {
        Appliance a = new Appliance();

        a.setAddress(addressRepository.findById(dto.addressId)
                .orElseThrow(() -> new NotFoundException("Address not found")));

        a.setApplianceType(applianceTypeRepository.findById(dto.applianceTypeId)
                .orElseThrow(() -> new NotFoundException("ApplianceType not found")));

        if (dto.brandId != null) {
            a.setBrand(brandRepository.findById(dto.brandId)
                    .orElseThrow(() -> new NotFoundException("Brand not found")));
        } else {
            a.setBrand(null);
        }

        a.setModel(dto.model);
        a.setSerialNumber(dto.serialNumber);

        a.setActive(dto.active == null ? true : dto.active);

        return a;
    }

    public Appliance update(ApplianceRequestDto dto, Appliance a) {

        a.setAddress(addressRepository.findById(dto.addressId)
                .orElseThrow(() -> new NotFoundException("Address not found: " + dto.addressId)));

        a.setApplianceType(applianceTypeRepository.findById(dto.applianceTypeId)
                .orElseThrow(() -> new NotFoundException("ApplianceType not found: " + dto.applianceTypeId)));

        if (dto.brandId != null) {
            a.setBrand(brandRepository.findById(dto.brandId)
                    .orElseThrow(() -> new NotFoundException("Brand not found: " + dto.brandId)));
        } else {
            a.setBrand(null);
        }

        a.setModel(dto.model);
        a.setSerialNumber(dto.serialNumber);
        a.setActive(dto.active == null ? true : dto.active);

        return a;
    }

    public static ApplianceResponseDto toResponse(Appliance a) {
        ApplianceResponseDto dto = new ApplianceResponseDto();
        dto.id = a.getId();

        dto.clientId = a.getAddress().getClient().getId();
        dto.clientName = a.getAddress().getClient().getName();
        dto.addressId = a.getAddress().getId();
        dto.addressName = a.getAddress().getAddress();

        dto.applianceTypeId = a.getApplianceType().getId();
        dto.applianceTypeName = a.getApplianceType().getName();
        dto.brandId = a.getBrand().getId();
        dto.brandName = a.getBrand().getName();
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

}
