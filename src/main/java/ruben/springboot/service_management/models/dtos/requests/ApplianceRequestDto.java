package ruben.springboot.service_management.models.dtos.requests;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ApplianceRequestDto {

    @NotNull(message = "applianceTypeId is required")
    @Positive(message = "applianceTypeId must be greater than 0")
    public Long applianceTypeId;

    @Positive(message = "brandId must be greater than 0")
    public Long brandId;

    @Size(max = 20, message = "model must be at most 20 characters")
    public String model;

    @Size(max = 45, message = "serialNumber must be at most 45 characters")
    public String serialNumber;

    public Boolean active;

}
