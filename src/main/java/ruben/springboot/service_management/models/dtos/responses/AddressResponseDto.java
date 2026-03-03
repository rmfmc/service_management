package ruben.springboot.service_management.models.dtos.responses;

import java.util.List;

import ruben.springboot.service_management.models.dtos.lists.ApplianceListDto;
import ruben.springboot.service_management.models.dtos.lists.ClientListDto;

public class AddressResponseDto {

    public Long id;
    public String address;
    public String city;
    public String province;
    public String postalCode;
    public ClientListDto client;
    public List<ApplianceListDto> appliances;
    
}

