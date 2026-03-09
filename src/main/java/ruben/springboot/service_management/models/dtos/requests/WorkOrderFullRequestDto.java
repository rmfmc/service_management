package ruben.springboot.service_management.models.dtos.requests;

import java.util.List;
import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class WorkOrderFullRequestDto {

    @Valid
    public ClientRequestDto client;
    public Long clientId;    
    
    @Valid
    public AddressRequestDto address;
    public Long addressId;

    @Valid
    public List<ApplianceRequestDto> newAppliances;
    public Set<Long> applianceIds; 

    @Valid
    @NotNull(message = "workOrder is required")
    public WorkOrderRequestDto workOrder;

    @Valid
    public List<WorkOrderChargeRequestDto> charges;

    public ClientRequestDto tenant;

}
