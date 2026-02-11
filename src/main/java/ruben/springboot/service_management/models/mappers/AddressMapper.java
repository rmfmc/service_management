package ruben.springboot.service_management.models.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ruben.springboot.service_management.errors.NotFoundException;
import ruben.springboot.service_management.models.Address;
import ruben.springboot.service_management.models.dtos.requests.AddressRequestDto;
import ruben.springboot.service_management.models.dtos.responses.AddressResponseDto;
import ruben.springboot.service_management.repositories.ClientRepository;

@Component
public class AddressMapper {

    @Autowired
    private ClientRepository clientRepository;

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

        dto.id = a.getId();
        dto.clientId = a.getClient().getId();
        dto.address = a.getAddress();
        dto.city = a.getCity();
        dto.province = a.getProvince();
        dto.postalCode = a.getPostalCode();

        return dto;
    }

}
