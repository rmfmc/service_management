package ruben.springboot.service_management.models.dtos.responses;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import ruben.springboot.service_management.models.dtos.lists.ClientListDto;
import ruben.springboot.service_management.models.dtos.lists.WorkOrderListDto;

public class WorkOrderChargeResponseDto {
    
    public Long id;
    public String chargeType;
    public String paymentMethod;
    public String description;
    public BigDecimal price;
    public String payer;
    public Boolean paid;
    public WorkOrderListDto workOrder;
    public ClientListDto client;

    public Long createdUserId;
    public String createdUserName;
    public LocalDateTime createdAt;
}

