package ruben.springboot.service_management.models.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AddressRequestDto {

    @NotBlank(message = "address is required")
    @Size(max = 100, message = "address must be at most 100 characters")
    public String address;

    @Size(max = 45, message = "city must be at most 45 characters")
    public String city;

    @Size(max = 45, message = "province must be at most 45 characters")
    public String province;

    @Size(max = 20, message = "postalCode must be at most 20 characters")
    public String postalCode;

}
