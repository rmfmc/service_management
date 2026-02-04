package ruben.springboot.service_management.models.dto;

import ruben.springboot.service_management.models.enums.ApplianceType;

public class ApplianceResponseDto {

    public Long id;
    public Long clientId;
    public String clientName;
    public String clientPhone;

    public ApplianceType applianceType;
    public String brand;
    public String model;
    public String serialNumber;
    public boolean active;
    
}
