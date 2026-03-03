package ruben.springboot.service_management.models.dtos.responses;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WorkOrderChargeOnlyResponseDto {
    
    public Long id;
    public String chargeType;
    public String paymentMethod;
    public String description;
    public BigDecimal price;
    public String payer;
    public Boolean paid;
    public Long createdUserId;
    public String createdUserName;
    public LocalDateTime createdAt;
}

