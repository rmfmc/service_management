package ruben.springboot.service_management.models.dtos.requests;

import jakarta.validation.constraints.Size;
import ruben.springboot.service_management.models.enums.WorkOrderStatus;

public class WorkOrderTechUpdateRequestDto {

    public WorkOrderStatus status;

    @Size(max = 250, message = "notes must be at most 250 characters")
    public String notes;

    @Size(max = 100, message = "workPerformed must be at most 100 characters")
    public String workPerformed;

    @Size(max = 45, message = "issueDescription must be at most 45 characters")
    public String billTo;

}
