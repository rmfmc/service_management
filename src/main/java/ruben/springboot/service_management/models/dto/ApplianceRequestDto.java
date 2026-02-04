package ruben.springboot.service_management.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ruben.springboot.service_management.models.enums.ApplianceType;
import jakarta.validation.constraints.NotNull;

public class ApplianceRequestDto {

    @NotNull(message = "clientId is required")
    public Long clientId;

    @NotNull(message = "applianceType is required")
    public ApplianceType applianceType;

    @Size(max = 60, message = "brand must be at most 60 characters")
    public String brand;

    @Size(max = 80, message = "model must be at most 80 characters")
    public String model;

    @Size(max = 80, message = "serialNumber must be at most 80 characters")
    public String serialNumber;

    public Boolean active;

}
