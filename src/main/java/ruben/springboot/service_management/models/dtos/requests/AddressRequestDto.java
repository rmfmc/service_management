package ruben.springboot.service_management.models.dtos.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AddressRequestDto {

    @NotNull
    public Long clientId;

    @NotNull
    @Size(max = 100)
    public String address;

    @Size(max = 45)
    public String city;

    @Size(max = 45)
    public String province;

    @Size(max = 20)
    public String postalCode;

}
