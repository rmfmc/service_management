package ruben.springboot.service_management.models.dtos.lists;

import java.time.LocalDate;
import java.time.LocalDateTime;

import ruben.springboot.service_management.models.enums.ApplianceType;
import ruben.springboot.service_management.models.enums.WorkOrderPriority;
import ruben.springboot.service_management.models.enums.WorkOrderStatus;

public class WorkOrderListDto {

    public Long id;
    public String issueDescription;
    public String status;
    public String priority;
    public int priorityInt;
    public LocalDate scheduledAt;
    public LocalDateTime createdAt;

    public String clientName;
    public String clientAddress;
    public String clientCity;
    public String clientPhone;

    public String applianceType;
    public String applianceBrand;

    public String assignedUserName;

    public WorkOrderListDto(Long id, String issueDescription, WorkOrderStatus status, WorkOrderPriority priority,
            LocalDate scheduledAt, LocalDateTime createdAt, String clientName, String clientAddress, String clientCity, String clientPhone,
            ApplianceType applianceType, String applianceBrand, String assignedUserName) {
        this.id = id;
        this.issueDescription = issueDescription;
        this.status = status.getLabelEs();
        this.priority = priority.getLabelEs();
        this.priorityInt = priority.getPriorityInt();
        this.scheduledAt = scheduledAt;
        this.createdAt = createdAt;
        this.clientName = clientName;
        this.clientAddress = clientAddress;
        this.clientCity = clientCity;
        this.clientPhone = clientPhone;
        if (applianceType != null)
            this.applianceType = applianceType.getLabelEs();
        this.applianceBrand = applianceBrand;
        this.assignedUserName = assignedUserName;
    }

    public WorkOrderListDto() {
    }

}
