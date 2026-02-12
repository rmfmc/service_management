package ruben.springboot.service_management.models.dtos.responses;

import java.util.List;

import ruben.springboot.service_management.models.dtos.lists.ApplianceListDto;

public class AddressResponseDto {

    public Long id;
    public Long clientId;
    public String clientName;
    public String address;
    public String city;
    public String province;
    public String postalCode;
    public List<ApplianceListDto> appliances;
    
}

