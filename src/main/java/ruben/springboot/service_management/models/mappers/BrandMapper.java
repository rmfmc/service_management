package ruben.springboot.service_management.models.mappers;

import ruben.springboot.service_management.models.Brand;
import ruben.springboot.service_management.models.dtos.BrandDto;

public class BrandMapper {

    public static Brand toEntity(BrandDto dto) {
        Brand b = new Brand();
        b.setName(dto.name);
        return b;
    }

    public static Brand update(BrandDto dto, Brand b) {
        b.setName(dto.name);
        return b;
    }

    public static BrandDto toDto(Brand b) {
        BrandDto dto = new BrandDto();
        dto.id = b.getId();
        dto.name = b.getName();
        return dto;
    }

}
