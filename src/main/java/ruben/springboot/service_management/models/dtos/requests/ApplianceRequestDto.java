package ruben.springboot.service_management.models.dtos.requests;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

public class ApplianceRequestDto {

    public Long id;

    @NotNull(message = "addressId is required")
    public Long addressId;

    @NotNull(message = "applianceTypeId is required")
    public Long applianceTypeId;

    public Long brandId;

    @Size(max = 20, message = "model must be at most 80 characters")
    public String model;

    @Size(max = 45, message = "serialNumber must be at most 80 characters")
    public String serialNumber;

    public Boolean active;

}
