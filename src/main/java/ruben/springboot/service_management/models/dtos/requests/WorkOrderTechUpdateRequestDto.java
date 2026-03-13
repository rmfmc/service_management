package ruben.springboot.service_management.models.dtos.requests;

import jakarta.validation.constraints.Size;
import ruben.springboot.service_management.models.enums.WorkOrderStatus;

public class WorkOrderTechUpdateRequestDto {

    public WorkOrderStatus status;

    @Size(max = 250, message = "las notas deben tener como máximo 250 caracteres")
    public String notes;

    @Size(max = 100, message = "el trabajo realizado debe tener como máximo 100 caracteres")
    public String workPerformed;

    @Size(max = 45, message = "el campo facturar a debe tener como máximo 45 caracteres")
    public String billTo;

}
