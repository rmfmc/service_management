package ruben.springboot.service_management.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BrandDto {

    public Long id;

    @NotBlank
    @Size(max = 45)
    public String name;
    
}
