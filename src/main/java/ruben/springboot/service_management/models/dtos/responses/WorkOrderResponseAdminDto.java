package ruben.springboot.service_management.models.dtos.responses;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class WorkOrderResponseAdminDto {

    public Long workOrderId;

    // Cliente
    public ClientOnlyResponseDto client;

    // Dirección (real)
    public AddressOnlyResponseDto address;

    // Inquilino
    public ClientOnlyResponseDto tenant;

    // Users (resumen)
    public String assignedUser;
    public String createdUser;
    public String lastUpdatedUser;

    // Aviso
    public String issueDescription;
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
    public List<ApplianceOnlyResponseDto> appliances;

    // Charges ordenados por createdAt ASC
    public List<WorkOrderChargeOnlyResponseDto> charges;
    
}

