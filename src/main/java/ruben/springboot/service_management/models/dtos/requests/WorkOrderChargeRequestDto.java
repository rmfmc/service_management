package ruben.springboot.service_management.models.dtos.requests;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import ruben.springboot.service_management.models.enums.ChargeType;
import ruben.springboot.service_management.models.enums.PaymentMethod;

public class WorkOrderChargeRequestDto {

    public Long workOrderId;

    public ChargeType chargeType;

    @Size(max = 45)
    public String description;

    @NotNull(message = "price is required")
    @PositiveOrZero
    public BigDecimal price;

    @Size(max = 45)
    public String payer;

    public Boolean paid;

    public PaymentMethod paymentMethod;
    
}
