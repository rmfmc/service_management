package ruben.springboot.service_management.models.mappers;

import ruben.springboot.service_management.models.Client;
import ruben.springboot.service_management.models.dtos.lists.ClientListDto;
import ruben.springboot.service_management.models.dtos.requests.ClientRequestDto;
import ruben.springboot.service_management.models.dtos.responses.AddressResponseDto;
import ruben.springboot.service_management.models.dtos.responses.ClientResponseDto;

public class ClientMapper {

    public static Client toEntity(ClientRequestDto dto) {
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

    public static Client update(ClientRequestDto dto, Client c) {
        c.setName(dto.name);
        c.setPhone(dto.phone.trim());
        c.setPhone2(dto.phone2);
        c.setPhone3(dto.phone3);
        c.setPhone4(dto.phone4);
        c.setEmail(dto.email);
        c.setNotes(dto.notes);
        return c;
    }

    public static ClientResponseDto toResponse(Client c) {
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
            dto.addresses = c.getAddresses().stream().map(AddressMapper::toResponse).toList();
        }
        return dto;
    }

    public static ClientListDto toListDto(Client c) {
        ClientListDto dto = new ClientListDto();
        dto.id = c.getId();
        dto.name = c.getName();
        dto.phone = c.getPhone();
        dto.phone2 = c.getPhone2();
        dto.address = c.getAddress();
        dto.city = c.getCity();
        return dto;
    }

}
