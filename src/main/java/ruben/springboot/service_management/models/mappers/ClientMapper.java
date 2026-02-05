package ruben.springboot.service_management.models.mappers;

import ruben.springboot.service_management.models.Client;
import ruben.springboot.service_management.models.dto.ClientListDto;
import ruben.springboot.service_management.models.dto.ClientRequestDto;
import ruben.springboot.service_management.models.dto.ClientResponseDto;

public class ClientMapper {

    public static Client toEntity(ClientRequestDto dto) {
        Client c = new Client();
        c.setName(dto.name);
        c.setAddress(dto.address);
        c.setCity(dto.city);
        c.setHasStairs(dto.hasStairs);
        c.setPhone(dto.phone.trim());
        c.setPhone2(dto.phone2);
        c.setEmail(dto.email);
        c.setNotes(dto.notes);
        return c;
    }

    public static ClientResponseDto toResponse(Client c) {
        ClientResponseDto dto = new ClientResponseDto();
        dto.id = c.getId();
        dto.name = c.getName();
        dto.address = c.getAddress();
        dto.city = c.getCity();
        dto.hasStairs = c.getHasStairs();
        dto.phone = c.getPhone();
        dto.phone2 = c.getPhone2();
        dto.email = c.getEmail();
        dto.notes = c.getNotes();
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
