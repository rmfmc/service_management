package ruben.springboot.service_management.models.dtos.lists;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class WorkOrderListDto {

    public Long id;
    public String issueDescription;
    public String status;
    public int priority;
    public LocalDate scheduledAt;
    public LocalDateTime createdAt;

    public String clientName;
    public String clientPhone;
    public String address;
    public String addressCity;

    public String applianceType;
    public String applianceBrand;

    public String assignedUserName;

    public WorkOrderListDto(Long id, String issueDescription, String status, int priority,
            LocalDate scheduledAt, LocalDateTime createdAt, String clientName, String clientPhone, String address,
            String addressCity, String applianceType, String applianceBrand, String assignedUserName) {
        this.id = id;
        this.issueDescription = issueDescription;
        this.status = status;
        this.priority = priority;
        this.priority = priority;
        this.scheduledAt = scheduledAt;
        this.createdAt = createdAt;
        this.clientName = clientName;
        this.clientPhone = clientPhone;
        this.address = address;
        this.addressCity = addressCity;
        this.applianceType = applianceType;
        this.applianceBrand = applianceBrand;
        this.assignedUserName = assignedUserName;
    }

    public WorkOrderListDto() {
    }

}
