package ruben.springboot.service_management.models.mappers;

import ruben.springboot.service_management.models.*;
import ruben.springboot.service_management.models.dtos.lists.WorkOrderListDto;
import ruben.springboot.service_management.models.dtos.requests.WorkOrderRequestDto;
import ruben.springboot.service_management.models.dtos.responses.WorkOrderResponseDto;
import ruben.springboot.service_management.models.enums.WorkOrderStatus;

import java.time.LocalDateTime;

public class WorkOrderMapper {

    public static WorkOrder toEntity(WorkOrderRequestDto req,
                                     Client client,
                                     Client owner,
                                     Appliance appliance,
                                     User assignedUser,
                                     User createdUser,
                                     User lastUpdatedUser) {

        WorkOrder w = new WorkOrder();
        w.setId(req.id);
        w.setClient(client);
        w.setOwner(owner);
        w.setAppliance(appliance);
        w.setAssignedUser(assignedUser);

        w.setCreatedUser(createdUser);
        w.setLastUpdatedUser(lastUpdatedUser);

        w.setIssueDescription(req.issueDescription);
        w.setStatus(req.status);
        w.setPriority(req.priority);
        w.setNotes(req.notes);
        w.setWorkPerformed(req.workPerformed);
        w.setPrice(req.price);
        w.setScheduledAt(req.sheduledAt);

        if ((req.status == WorkOrderStatus.CLOSED || req.status == WorkOrderStatus.APPLIANCE_INSTALLED) && w.getClosedAt() == null) {
            w.setClosedAt(LocalDateTime.now());
        }

        return w;
    }


    public static WorkOrderResponseDto toResponse(WorkOrder w) {
        WorkOrderResponseDto dto = new WorkOrderResponseDto();

        dto.id = w.getId();

        dto.clientId = w.getClient().getId();
        dto.clientName = w.getClient().getName();
        dto.clientPhone = w.getClient().getPhone();

        if (w.getOwner() != null) {
            dto.ownerId = w.getOwner().getId();
            dto.ownerName = w.getOwner().getName();
            dto.ownerPhone = w.getOwner().getPhone();
        }

        if (w.getAppliance() != null) {
            dto.applianceId = w.getAppliance().getId();
            dto.applianceType = w.getAppliance().getApplianceType().getLabelEs();
            dto.applianceBrand = w.getAppliance().getBrand();
            dto.applianceModel = w.getAppliance().getModel();
        }

        if (w.getAssignedUser() != null) {
            dto.assignedUserId = w.getAssignedUser().getId();
            dto.assignedUserName = w.getAssignedUser().getName();
        }

        dto.createdUserId = w.getCreatedUser().getId();
        dto.createdUserName = w.getCreatedUser().getName();

        dto.lastUpdatedUserId = w.getLastUpdatedUser().getId();
        dto.lastUpdatedUserName = w.getLastUpdatedUser().getName();

        dto.issueDescription = w.getIssueDescription();

        dto.status = w.getStatus().getLabelEs();
        dto.priority = w.getPriority().getLabelEs();

        dto.notes = w.getNotes();
        dto.workPerformed = w.getWorkPerformed();
        dto.price = w.getPrice();
        dto.scheduledAt = w.getScheduledAt();

        dto.createdAt = w.getCreatedAt();
        dto.closedAt = w.getClosedAt();
        dto.lastUpdatedAt = w.getLastUpdatedAt();

        return dto;
    }

    public static WorkOrderListDto toList(WorkOrder w) {
        WorkOrderListDto dto = new WorkOrderListDto();

        dto.id = w.getId();

        dto.clientName = w.getClient().getName();
        dto.clientPhone = w.getClient().getPhone();
        dto.clientAddress = w.getClient().getAddress();
        dto.clientCity = w.getClient().getCity();

        if (w.getAppliance() != null) {
            dto.applianceType = w.getAppliance().getApplianceType().getLabelEs();
            dto.applianceBrand = w.getAppliance().getBrand();
        }

        if (w.getAssignedUser() != null) {
            dto.assignedUserName = w.getAssignedUser().getName();
        }

        dto.issueDescription = w.getIssueDescription();
        dto.status = w.getStatus().getLabelEs();
        dto.priority = w.getPriority().getLabelEs();
        dto.priorityInt = w.getPriority().getPriorityInt();

        dto.scheduledAt = w.getScheduledAt();
        dto.createdAt = w.getCreatedAt();

        return dto;
    }

}
