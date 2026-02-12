package ruben.springboot.service_management.models.dtos.requests;

import java.util.List;
import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class WorkOrderFullRequestDto {

    @Valid
    @NotNull(message = "client is required")
    public ClientRequestDto clientDto;
    public Long clientId;    
    
    @Valid
    @NotNull(message = "address is required")
    public AddressRequestDto addressDto;
    public Long addressId;

    @Valid
    public List<ApplianceRequestDto> newAppliances;
    public Set<Long> applianceIds; 

    @Valid
    @NotNull(message = "workOrder is required")
    public WorkOrderRequestDto workOrderDto;
    public Long workOrderId;

    @Valid
    public List<WorkOrderChargeRequestDto> charges;

    public ClientRequestDto tenantDto;

}
