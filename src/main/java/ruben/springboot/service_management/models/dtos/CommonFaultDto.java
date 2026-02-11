package ruben.springboot.service_management.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CommonFaultDto {

    public Long id;

    @NotNull
    public Long applianceTypeId;

    @NotBlank
    @Size(max = 45)
    public String name;

}
