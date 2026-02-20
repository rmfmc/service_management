package ruben.springboot.service_management.models.dtos.responses;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class WorkOrderResponseAdminDto {

    public Long workOrderId;

    // Cliente
    public Long clientId;
    public String clientName;
    public String clientPhone;

    // Dirección (real)
    public Long addressId;
    public String address;
    public String city;
    public String province;
    public String postalCode;

    // Inquilino (si lo usas como Client)
    public Long tenantId;
    public String tenantName;
    public String tenantPhone;

    // Users (resumen)
    public String assignedUser;
    public String createdUser;
    public String lastUpdatedUser;

    // Datos del aviso
    public String issueDescription;

    // Si en tu response quieres label en español:
    public String status;
    public int priority;
    public String notes;
    public String workPerformed;

    public Boolean discountVisit;
    public String billTo;

    public BigDecimal totalPrice;

    public LocalDate scheduledAt;

    public LocalDateTime createdAt;
    public LocalDateTime closedAt;
    public LocalDateTime lastUpdatedAt;

    // Relación con aparatos
    public List<ApplianceResponseDto> appliances;

    // Charges ordenados por createdAt ASC
    public List<WorkOrderChargeResponseDto> charges;
    
}

