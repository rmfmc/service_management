package ruben.springboot.service_management.models.mappers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ruben.springboot.service_management.errors.NotFoundException;
import ruben.springboot.service_management.models.Address;
import ruben.springboot.service_management.models.Appliance;
import ruben.springboot.service_management.models.Client;
import ruben.springboot.service_management.models.dtos.lists.AddressListDto;
import ruben.springboot.service_management.models.dtos.requests.AddressRequestDto;
import ruben.springboot.service_management.models.dtos.responses.AddressResponseDto;
import ruben.springboot.service_management.repositories.ApplianceRepository;
import ruben.springboot.service_management.repositories.ClientRepository;

@Component
public class AddressMapper {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ApplianceRepository applianceRepository;

    public Address toEntity(AddressRequestDto dto) {

        Address a = new Address();
        a.setClient(clientRepository.findById(dto.clientId)
                .orElseThrow(() -> new NotFoundException("client not found")));
        a.setAddress(dto.address);
        a.setCity(dto.city);
        a.setProvince(dto.province);
        a.setPostalCode(dto.postalCode);
        return a;

    }

    public Address update(AddressRequestDto dto, Address a) {
        a.setClient(clientRepository.findById(dto.clientId)
                .orElseThrow(() -> new NotFoundException("Client not found: " + dto.clientId)));
        a.setAddress(dto.address);
        a.setCity(dto.city);
        a.setProvince(dto.province);
        a.setPostalCode(dto.postalCode);
        return a;

    }

    public AddressResponseDto toResponse(Address a) {
        AddressResponseDto dto = new AddressResponseDto();

        Client c = a.getClient();

        dto.id = a.getId();
        dto.clientId = c.getId();
        dto.clientName = c.getName();
        dto.clientPhone = c.getPhone();
        dto.address = a.getAddress();
        dto.city = a.getCity();
        dto.province = a.getProvince();
        dto.postalCode = a.getPostalCode();
        dto.appliances = 0;

        List<Appliance> appliances = applianceRepository.findByAddressId(a.getId());

        if (!appliances.isEmpty()) {
            dto.appliances = appliances.size();
        }

        return dto;
    }

    public AddressListDto toList(Address a){
        AddressListDto dto = new AddressListDto();

        dto.id = a.getId();
        dto.address = a.getAddress();
        dto.city = a.getCity();
        dto.province = a.getProvince();
        dto.postal_code = a.getPostalCode();

        return dto;

    }

}
