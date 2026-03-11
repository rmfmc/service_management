package ruben.springboot.service_management.models.dtos.requests;

import java.util.List;
import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class WorkOrderFullRequestDto {

    @Valid
    public ClientRequestDto client;
    @Positive(message = "clientId must be greater than 0")
    public Long clientId;    
    
    @Valid
    public AddressRequestDto address;
    @Positive(message = "addressId must be greater than 0")
    public Long addressId;

    @Valid
    public List<ApplianceRequestDto> newAppliances;
    public Set<@Positive(message = "applianceIds values must be greater than 0") Long> applianceIds;

    @Valid
    @NotNull(message = "workOrder is required")
    public WorkOrderRequestDto workOrder;

    @Valid
    public List<WorkOrderChargeRequestDto> charges;

    @Valid
    public ClientRequestDto tenant;
    @Positive(message = "tenantId must be greater than 0")
    public Long tenantId;

}
