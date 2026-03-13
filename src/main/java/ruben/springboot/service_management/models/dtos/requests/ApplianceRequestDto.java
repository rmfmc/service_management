package ruben.springboot.service_management.models.dtos.requests;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ApplianceRequestDto {

    @NotNull(message = "el id del tipo de electrodoméstico es obligatorio")
    @Positive(message = "el id del tipo de electrodoméstico debe ser mayor que 0")
    public Long applianceTypeId;

    @Positive(message = "el id de la marca debe ser mayor que 0")
    public Long brandId;

    @Size(max = 20, message = "el modelo debe tener como máximo 20 caracteres")
    public String model;

    @Size(max = 45, message = "el número de serie debe tener como máximo 45 caracteres")
    public String serialNumber;

    public Boolean active;

}
