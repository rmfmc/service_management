package ruben.springboot.service_management.models.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import ruben.springboot.service_management.models.enums.WorkOrderStatus;

public class WorkOrderResponseDto {
    
    public Long id;

    public Long clientId;
    public String clientName;
    public String clientPhone;

    public Long ownerId;
    public String ownerName;
    public String ownerPhone;

    public Long applianceId;
    public String applianceType;
    public String applianceBrand;
    public String applianceModel;

    public Long assignedUserId;
    public String assignedUserName;

    public Long createdUserId;
    public String createdUserName;

    public Long lastUpdatedUserId;
    public String lastUpdatedUserName;

    public String issueDescription;

    public WorkOrderStatus status;
    public int priority; // valor 1..4
    public String priorityLabel; // LOW/MEDIUM...

    public String notes;
    public String workPerformed;
    public BigDecimal price;

    public LocalDateTime createdAt;
    public LocalDateTime closedAt;
    public LocalDateTime lastUpdatedAt;

}
