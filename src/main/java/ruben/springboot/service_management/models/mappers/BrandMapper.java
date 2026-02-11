package ruben.springboot.service_management.models.mappers;

import ruben.springboot.service_management.models.Brand;
import ruben.springboot.service_management.models.dtos.requests.BrandRequestDto;

public class BrandMapper {

    public static Brand toEntity(BrandRequestDto dto) {
        Brand b = new Brand();
        b.setName(dto.name);
        return b;
    }

}
