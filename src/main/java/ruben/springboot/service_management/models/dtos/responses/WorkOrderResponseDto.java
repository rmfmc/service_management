package ruben.springboot.service_management.models.dtos.responses;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    public String status;
    public String priority;

    public String notes;
    public String workPerformed;
    public BigDecimal price;
    public LocalDate scheduledAt;

    public LocalDateTime createdAt;
    public LocalDateTime closedAt;
    public LocalDateTime lastUpdatedAt;

}
