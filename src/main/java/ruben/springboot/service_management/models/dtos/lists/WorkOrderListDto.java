package ruben.springboot.service_management.models.dtos.lists;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

}
