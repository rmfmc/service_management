package ruben.springboot.service_management.models.dtos.requests;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ruben.springboot.service_management.models.enums.ChargeType;
import ruben.springboot.service_management.models.enums.PaymentMethod;

public class WorkOrderChargeRequestDto {
    
    @NotNull(message = "chargeType is required")
    public ChargeType chargeType;

    @Size(max = 45, message = "description must be at most 45 characters")
    public String description;

    @NotNull(message = "price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "price must be greater than or equal to 0")
    public BigDecimal price;

    @Size(max = 45, message = "payer must be at most 45 characters")
    public String payer;

    public Boolean paid;

    @NotNull(message = "paymentMethod is required")
    public PaymentMethod paymentMethod;
    
}
