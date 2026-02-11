package ruben.springboot.service_management.models.dtos.lists;

import ruben.springboot.service_management.models.enums.ApplianceType;

public class ApplianceListDto {

    public Long id;
    public String type;
    public String brand;
    public String model;
    public String serialNumber;
    public boolean active;
    
    public ApplianceListDto(Long id, ApplianceType type, String brand, String model, String serialNumber, boolean active) {
        this.id = id;
        this.type = type.getLabelEs();
        this.brand = brand;
        this.model = model;
        this.serialNumber = serialNumber;
        this.active = active;
    }

    public ApplianceListDto() {
    }

    
}
