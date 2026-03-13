package ruben.springboot.service_management.models.dtos.requests;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ruben.springboot.service_management.models.enums.ChargeType;
import ruben.springboot.service_management.models.enums.PaymentMethod;

public class WorkOrderChargeRequestDto {
    
    @NotNull(message = "el tipo de cargo es obligatorio")
    public ChargeType chargeType;

    @Size(max = 45, message = "la descripción debe tener como máximo 45 caracteres")
    public String description;

    @NotNull(message = "el precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "el precio debe ser mayor o igual que 0")
    public BigDecimal price;

    @Size(max = 45, message = "el pagador debe tener como máximo 45 caracteres")
    public String payer;

    public Boolean paid;

    @NotNull(message = "el método de pago es obligatorio")
    public PaymentMethod paymentMethod;
    
}
