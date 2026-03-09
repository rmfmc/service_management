package ruben.springboot.service_management.models.mappers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ruben.springboot.service_management.errors.NotFoundException;
import ruben.springboot.service_management.models.Address;
import ruben.springboot.service_management.models.Appliance;
import ruben.springboot.service_management.models.dtos.lists.AddressListDto;
import ruben.springboot.service_management.models.dtos.requests.AddressRequestDto;
import ruben.springboot.service_management.models.dtos.responses.AddressOnlyResponseDto;
import ruben.springboot.service_management.models.dtos.responses.AddressResponseDto;
import ruben.springboot.service_management.repositories.ApplianceRepository;
import ruben.springboot.service_management.repositories.ClientRepository;

@Component
public class AddressMapper {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ApplianceRepository applianceRepository;

    public Address toEntity(AddressRequestDto dto, Long clientId) {

        Address a = new Address();
        a.setClient(clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("client not found")));
        a.setAddress(dto.address);
        a.setCity(dto.city);
        a.setProvince(dto.province);
        a.setPostalCode(dto.postalCode);
        return a;

    }

    public Address update(Address a, AddressRequestDto dto, Long clientId) {
        a.setClient(clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Client not found: " + clientId)));
        a.setAddress(dto.address);
        a.setCity(dto.city);
        a.setProvince(dto.province);
        a.setPostalCode(dto.postalCode);
        return a;

    }

    public AddressResponseDto toResponse(Address a) {
        AddressResponseDto dto = new AddressResponseDto();

        dto.id = a.getId();
        dto.address = a.getAddress();
        dto.city = a.getCity();
        dto.province = a.getProvince();
        dto.postalCode = a.getPostalCode();
        dto.client = ClientMapper.toList(a.getClient());
        
        List<Appliance> appliances = applianceRepository.findByAddressId(a.getId());
        if (!appliances.isEmpty()) {
            dto.appliances = appliances.stream().map(ApplianceMapper::toList).toList();
        }

        return dto;
    }

    public AddressOnlyResponseDto toOnlyResponse(Address a) {
        AddressOnlyResponseDto dto = new AddressOnlyResponseDto();

        dto.id = a.getId();
        dto.address = a.getAddress();
        dto.city = a.getCity();
        dto.province = a.getProvince();
        dto.postalCode = a.getPostalCode();
        dto.appliances = applianceRepository.findByAddressId(a.getId()).size();

        return dto;
    }

    public static AddressListDto toList(Address a){
        AddressListDto dto = new AddressListDto();

        dto.id = a.getId();
        dto.address = a.getAddress();
        dto.city = a.getCity();
        dto.province = a.getProvince();
        dto.postal_code = a.getPostalCode();

        return dto;

    }

}
