package ruben.springboot.service_management.models.dtos.requests;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import ruben.springboot.service_management.models.enums.WorkOrderStatus;

public class WorkOrderRequestDto {

    public Long createdUserId;
    public Long lastUpdateUserId;
    public Long assignedUserId;

    @Size(max = 100, message = "issueDescription must be at most 100 characters")
    public String issueDescription;

    public WorkOrderStatus status;

    @PositiveOrZero(message = "priority must be between 1 and 4")
    public Integer priority;

    @Size(max = 250, message = "notes must be at most 250 characters")
    public String notes;

    @Size(max = 100, message = "workPerformed must be at most 100 characters")
    public String workPerformed;

    public Boolean discountVisit;

    public BigDecimal totalPrice;

    @Size(max = 45, message = "issueDescription must be at most 45 characters")
    public String billTo;

    public LocalDateTime createdAt;

    @NotNull(message = "lastUpdateAt is required")
    public LocalDateTime lastUpdateAt;

    @NotNull(message = "shceduledAt is required")
    public LocalDate scheduledAt;

}
