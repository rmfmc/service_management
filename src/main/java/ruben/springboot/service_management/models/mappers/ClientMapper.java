package ruben.springboot.service_management.models.mappers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ruben.springboot.service_management.models.Address;
import ruben.springboot.service_management.models.Client;
import ruben.springboot.service_management.models.dtos.lists.AddressListDto;
import ruben.springboot.service_management.models.dtos.lists.ClientListDto;
import ruben.springboot.service_management.models.dtos.requests.ClientRequestDto;
import ruben.springboot.service_management.models.dtos.responses.ClientOnlyResponseDto;
import ruben.springboot.service_management.models.dtos.responses.ClientResponseDto;

@Service
public class ClientMapper {

    @Autowired
    private AddressMapper addressMapper;

    public Client toEntity(ClientRequestDto dto) {
        Client c = new Client();
        c.setName(dto.name);
        c.setPhone(dto.phone.trim());
        c.setPhone2(dto.phone2);
        c.setPhone3(dto.phone3);
        c.setPhone4(dto.phone4);
        c.setEmail(dto.email);
        c.setNotes(dto.notes);
        return c;
    }

    public Client update(ClientRequestDto dto, Client c) {
        c.setName(dto.name);
        c.setPhone(dto.phone.trim());
        c.setPhone2(dto.phone2);
        c.setPhone3(dto.phone3);
        c.setPhone4(dto.phone4);
        c.setEmail(dto.email);
        c.setNotes(dto.notes);
        return c;
    }

    public ClientResponseDto toResponse(Client c) {
        ClientResponseDto dto = new ClientResponseDto();

        dto.id = c.getId();
        dto.name = c.getName();
        dto.phone = c.getPhone();
        dto.phone2 = c.getPhone2();
        dto.phone3 = c.getPhone3();
        dto.phone4 = c.getPhone4();
        dto.email = c.getEmail();
        dto.notes = c.getNotes();
        dto.createdAt = c.getCreatedAt();

        if (c.getAddresses() != null) {
            List<AddressListDto> adresses = new ArrayList<>();
            adresses = c.getAddresses().stream().map(addressMapper::toList).toList();
            dto.addresses = adresses;
        }
        
        return dto;
    }

    public static ClientOnlyResponseDto toOnlyResponse(Client c) {
        ClientOnlyResponseDto dto = new ClientOnlyResponseDto();

        dto.id = c.getId();
        dto.name = c.getName();
        dto.phone = c.getPhone();
        dto.phone2 = c.getPhone2();
        dto.phone3 = c.getPhone3();
        dto.phone4 = c.getPhone4();
        dto.email = c.getEmail();
        dto.notes = c.getNotes();
        dto.createdAt = c.getCreatedAt();
        
        return dto;
    }

    public ClientListDto toListDto(Client c) {
        ClientListDto dto = new ClientListDto();
        dto.id = c.getId();
        dto.name = c.getName();
        dto.phone = c.getPhone();
        dto.phone2 = c.getPhone2();

        if (c.getAddresses() != null) {
            
            ArrayList<String> addressesNames = new ArrayList<>();
            ArrayList<String> addressesCities = new ArrayList<>();
            
            for (Address a : c.getAddresses()) {
                addressesNames.add(a.getAddress());
                addressesCities.add(a.getCity());
            }

            dto.addressesNames = addressesNames;
            dto.addressesCities = addressesCities;

        }

        return dto;
    }

}
