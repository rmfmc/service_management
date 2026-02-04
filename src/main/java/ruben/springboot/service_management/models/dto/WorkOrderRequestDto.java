package ruben.springboot.service_management.models.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ruben.springboot.service_management.models.enums.WorkOrderPriority;
import ruben.springboot.service_management.models.enums.WorkOrderStatus;

public class WorkOrderRequestDto {
    @NotNull(message = "clientId is required")
    public Long clientId;

    public Long ownerId;

    public Long applianceId;

    public Long assignedUserId;

    @Size(max = 100, message = "issueDescription must be at most 100 characters")
    public String issueDescription;

    @NotNull(message = "status is required")
    public WorkOrderStatus status;

    @NotNull(message = "priority is required")
    public WorkOrderPriority priority;

    @Size(max = 250, message = "notes must be at most 250 characters")
    public String notes;

    @Size(max = 100, message = "workPerformed must be at most 100 characters")
    public String workPerformed;

    public BigDecimal price;

}
