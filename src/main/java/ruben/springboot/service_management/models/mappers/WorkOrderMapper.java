package ruben.springboot.service_management.models.mappers;

import ruben.springboot.service_management.models.*;
import ruben.springboot.service_management.models.dto.WorkOrderRequestDto;
import ruben.springboot.service_management.models.dto.WorkOrderResponseDto;
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
        w.setClient(client);
        w.setOwner(owner);
        w.setAppliance(appliance);
        w.setAssignedUser(assignedUser);

        w.setCreatedUser(createdUser);
        w.setLastUpdatedUser(lastUpdatedUser);

        w.setIssueDescription(req.issueDescription);
        w.setStatus(req.status.name());
        w.setPriority(req.priority.getValue());
        w.setNotes(req.notes);
        w.setWorkPerformed(req.workPerformed);
        w.setPrice(req.price);

        // Si entra ya como CLOSED, cerramos fecha
        if (req.status == WorkOrderStatus.CLOSED) {
            w.setClosedAt(LocalDateTime.now());
        }

        return w;
    }

    public static void updateEntity(WorkOrder w,
                                   WorkOrderRequestDto req,
                                   Client client,
                                   Client owner,
                                   Appliance appliance,
                                   User assignedUser,
                                   User lastUpdatedUser) {

        w.setClient(client);
        w.setOwner(owner);
        w.setAppliance(appliance);
        w.setAssignedUser(assignedUser);

        w.setLastUpdatedUser(lastUpdatedUser);

        w.setIssueDescription(req.issueDescription);
        w.setStatus(req.status.name());
        w.setPriority(req.priority.getValue());
        w.setNotes(req.notes);
        w.setWorkPerformed(req.workPerformed);
        w.setPrice(req.price);

        if (req.status == WorkOrderStatus.CLOSED && w.getClosedAt() == null) {
            w.setClosedAt(LocalDateTime.now());
        }

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

        dto.status = WorkOrderStatus.valueOf(w.getStatus());
        dto.priority = w.getPriority();
        dto.priorityLabel = priorityLabel(w.getPriority());

        dto.notes = w.getNotes();
        dto.workPerformed = w.getWorkPerformed();
        dto.price = w.getPrice();

        dto.createdAt = w.getCreatedAt();
        dto.closedAt = w.getClosedAt();
        dto.lastUpdatedAt = w.getLastUpdatedAt();

        return dto;
    }

    private static String priorityLabel(int p) {
        return switch (p) {
            case 1 -> "LOW";
            case 2 -> "MEDIUM";
            case 3 -> "HIGH";
            case 4 -> "URGENT";
            default -> "UNKNOWN";
        };
    }
}
