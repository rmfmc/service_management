package ruben.springboot.service_management.models.dtos.requests;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import ruben.springboot.service_management.models.enums.WorkOrderStatus;

public class WorkOrderRequestDto {

    @Positive(message = "createdUserId must be greater than 0")
    public Long createdUserId;
    @Positive(message = "lastUpdateUserId must be greater than 0")
    public Long lastUpdateUserId;
    @Positive(message = "assignedUserId must be greater than 0")
    public Long assignedUserId;

    @Size(max = 100, message = "issueDescription must be at most 100 characters")
    public String issueDescription;

    public WorkOrderStatus status;

    @Min(value = 1, message = "priority must be between 1 and 4")
    @Max(value = 4, message = "priority must be between 1 and 4")
    public Integer priority;

    @Size(max = 250, message = "notes must be at most 250 characters")
    public String notes;

    @Size(max = 100, message = "workPerformed must be at most 100 characters")
    public String workPerformed;

    public Boolean discountVisit;

    @Size(max = 45, message = "billTo must be at most 45 characters")
    public String billTo;

    public LocalDateTime createdAt;

    @NotNull(message = "lastUpdateAt is required")
    public LocalDateTime lastUpdateAt;

    @NotNull(message = "scheduledAt is required")
    public LocalDate scheduledAt;

}
