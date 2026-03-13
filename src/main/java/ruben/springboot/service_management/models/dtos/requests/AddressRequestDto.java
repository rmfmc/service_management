package ruben.springboot.service_management.models.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AddressRequestDto {

    @NotBlank(message = "la dirección es obligatoria")
    @Size(max = 100, message = "la dirección debe tener como máximo 100 caracteres")
    public String address;

    @Size(max = 45, message = "la ciudad debe tener como máximo 45 caracteres")
    public String city;

    @Size(max = 45, message = "la provincia debe tener como máximo 45 caracteres")
    public String province;

    @Size(max = 20, message = "el código postal debe tener como máximo 20 caracteres")
    public String postalCode;

}
