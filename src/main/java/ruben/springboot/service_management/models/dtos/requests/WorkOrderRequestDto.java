package ruben.springboot.service_management.models.dtos.requests;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import ruben.springboot.service_management.models.enums.WorkOrderStatus;

public class WorkOrderRequestDto {

    @Positive(message = "el id del usuario asignado debe ser mayor que 0")
    public Long assignedUserId;

    @Size(max = 100, message = "la descripción de la avería debe tener como máximo 100 caracteres")
    public String issueDescription;

    public WorkOrderStatus status;

    @Min(value = 1, message = "la prioridad debe estar entre 1 y 4")
    @Max(value = 4, message = "la prioridad debe estar entre 1 y 4")
    public Integer priority;

    @Size(max = 250, message = "las notas deben tener como máximo 250 caracteres")
    public String notes;

    @Size(max = 100, message = "el trabajo realizado debe tener como máximo 100 caracteres")
    public String workPerformed;

    public Boolean discountVisit;

    @Size(max = 45, message = "el campo facturar a debe tener como máximo 45 caracteres")
    public String billTo;

    @NotNull(message = "la fecha programada es obligatoria")
    public LocalDate scheduledAt;

}
