package ruben.springboot.service_management.models.dtos.requests;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import ruben.springboot.service_management.models.enums.WorkOrderStatus;

public class WorkOrderRequestDto {

    @NotNull(message = "clientId is required")
    public Long clientId;

    @NotNull(message = "addressId is required")
    public Long addressId;

    public Set<Long> applianceIds;

    public Long createdUserId;
    public Long lastUpdateUserId;
    public Long assignedUserId;

    @Size(max = 100, message = "issueDescription must be at most 100 characters")
    public String issueDescription;

    public WorkOrderStatus status;

    @PositiveOrZero(message = "priority must be between 0 and 3")
    @NotNull(message = "priority is required")
    public int priority;

    @Size(max = 250, message = "notes must be at most 250 characters")
    public String notes;

    @Size(max = 100, message = "workPerformed must be at most 100 characters")
    public String workPerformed;

    public Boolean discountVisit;

    public BigDecimal totalPrice;

    @Size(max = 45, message = "issueDescription must be at most 45 characters")
    public String billTo;

    @Valid
    public List<WorkOrderChargeRequestDto> charges;

    public Long tenantId;

    @NotNull(message = "shceduledAt is required")
    public LocalDate scheduledAt;

}
