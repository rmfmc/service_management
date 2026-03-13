package ruben.springboot.service_management.models.dtos.responses;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class WorkOrderResponseDto {

    public Long workOrderId;

    public ClientOnlyResponseDto client;

    public AddressOnlyResponseDto address;

    public ClientOnlyResponseDto tenant;

    public String assignedUser;
    public String createdUser;
    public String lastUpdatedUser;

    public String issueDescription;
    public String status;
    public String priority;
    public String notes;
    public String workPerformed;
    public Boolean discountVisit;
    public String billTo;
    public BigDecimal totalPrice;

    public LocalDate scheduledAt;
    public LocalDateTime createdAt;
    public LocalDateTime closedAt;
    public LocalDateTime lastUpdatedAt;

    public List<ApplianceOnlyResponseDto> appliances;

    public List<WorkOrderChargeOnlyResponseDto> charges;
    
}

