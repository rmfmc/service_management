package ruben.springboot.service_management.models.dtos.responses;

import ruben.springboot.service_management.models.dtos.lists.AddressListDto;
import ruben.springboot.service_management.models.dtos.lists.ClientListDto;

public class ApplianceResponseDto {

    public Long id;
    public String applianceTypeName;
    public String brandName;
    public String model;
    public String serialNumber;
    public boolean active;
    public AddressListDto address;
    public ClientListDto client;

}
