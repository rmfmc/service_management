package ruben.springboot.service_management.models.dtos.requests;

import java.util.List;
import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class WorkOrderFullRequestDto {

    @Valid
    public ClientRequestDto client;
    @Positive(message = "el id del cliente debe ser mayor que 0")
    public Long clientId;    
    
    @Valid
    public AddressRequestDto address;
    @Positive(message = "el id de la dirección debe ser mayor que 0")
    public Long addressId;

    @Valid
    public List<ApplianceRequestDto> newAppliances;
    public Set<@Positive(message = "los valores de applianceIds deben ser mayores que 0") Long> applianceIds;

    @Valid
    @NotNull(message = "el aviso es obligatorio")
    public WorkOrderRequestDto workOrder;

    @Valid
    public List<WorkOrderChargeRequestDto> charges;

    @Valid
    public ClientRequestDto tenant;
    @Positive(message = "el id del inquilino debe ser mayor que 0")
    public Long tenantId;

}
