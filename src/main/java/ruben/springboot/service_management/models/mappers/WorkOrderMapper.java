package ruben.springboot.service_management.models.mappers;

import ruben.springboot.service_management.models.*;
import ruben.springboot.service_management.models.dtos.lists.WorkOrderListDto;
import ruben.springboot.service_management.models.dtos.requests.WorkOrderTechUpdateRequestDto;
import ruben.springboot.service_management.models.dtos.responses.WorkOrderResponseDto;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkOrderMapper {

    @Autowired
    private AddressMapper addressMapper;

    public WorkOrderResponseDto toResponse(WorkOrder w) {

        WorkOrderResponseDto dto = new WorkOrderResponseDto();

        dto.workOrderId = w.getId();

        Client client = w.getClient();
        if (client != null) {
            dto.client = ClientMapper.toOnlyResponse(client);
        }

        Address address = w.getAddress();
        if (address != null) {
            dto.address = addressMapper.toOnlyResponse(address);
        }

        Client tenant = w.getTenant();
        if (tenant != null) {
            dto.tenant = ClientMapper.toOnlyResponse(tenant);
        }

        if (w.getAssignedUser() != null) {
            dto.assignedUser = w.getAssignedUser().getName();
        }

        if (w.getCreatedUser() != null) {
            dto.createdUser = w.getCreatedUser().getName();
        }

        if (w.getLastUpdatedUser() != null) {
            dto.lastUpdatedUser = w.getLastUpdatedUser().getName();
        }

        dto.issueDescription = w.getIssueDescription();
        dto.status = w.getStatus().getLabelEs();
        dto.priority = w.getPriority();
        dto.notes = w.getNotes();
        dto.workPerformed = w.getWorkPerformed();
        dto.discountVisit = w.getDiscountVisit();
        dto.billTo = w.getBillTo();
        dto.totalPrice = w.getTotalPrice();

        dto.scheduledAt = w.getScheduledAt();
        dto.createdAt = w.getCreatedAt();
        dto.closedAt = w.getClosedAt();
        dto.lastUpdatedAt = w.getLastUpdatedAt();

        dto.appliances = new ArrayList<>();
        if (w.getAppliances() != null) {
            for (Appliance a : w.getAppliances()) {
                dto.appliances.add(ApplianceMapper.toOnlyResponse(a));
            }
        }

        dto.charges = new ArrayList<>();
        if (w.getCharges() != null) {
            for (WorkOrderCharge ch : w.getCharges()) {
                dto.charges.add(WorkOrderChargeMapper.toOnlyResponse(ch));
            }
        }

        return dto;
    }

    public WorkOrderResponseDto toTechResponse(WorkOrder w) {

        WorkOrderResponseDto dto = new WorkOrderResponseDto();

        dto.workOrderId = w.getId();

        Client client = w.getClient();
        if (client != null) {
            dto.client = ClientMapper.toOnlyResponse(client);
        }

        Address address = w.getAddress();
        if (address != null) {
            dto.address = addressMapper.toOnlyResponse(address);
        }

        Client tenant = w.getTenant();
        if (tenant != null) {
            dto.tenant = ClientMapper.toOnlyResponse(tenant);
        }

        if (w.getAssignedUser() != null) {
            dto.assignedUser = w.getAssignedUser().getName();
        }

        dto.issueDescription = w.getIssueDescription();
        dto.status = w.getStatus().getLabelEs();
        dto.priority = w.getPriority();
        dto.notes = w.getNotes();
        dto.workPerformed = w.getWorkPerformed();
        dto.discountVisit = w.getDiscountVisit();
        dto.billTo = w.getBillTo();
        dto.totalPrice = w.getTotalPrice();

        dto.scheduledAt = w.getScheduledAt();

        dto.appliances = new ArrayList<>();
        if (w.getAppliances() != null) {
            for (Appliance a : w.getAppliances()) {
                dto.appliances.add(ApplianceMapper.toOnlyResponse(a));
            }
        }

        dto.charges = new ArrayList<>();
        if (w.getCharges() != null) {
            for (WorkOrderCharge ch : w.getCharges()) {
                dto.charges.add(WorkOrderChargeMapper.toOnlyResponse(ch));
            }
        }

        return dto;
    }

    public static WorkOrderListDto toList(WorkOrder w) {

        WorkOrderListDto dto = new WorkOrderListDto();

        dto.id = w.getId();
        dto.issueDescription = w.getIssueDescription();

        if (w.getStatus() != null)
            dto.status = w.getStatus().getLabelEs();

        dto.priority = w.getPriority();

        dto.scheduledAt = w.getScheduledAt();
        dto.createdAt = w.getCreatedAt();

        Client c = w.getClient();
        if (c != null) {
            dto.clientName = c.getName();
            dto.clientPhone = c.getPhone();
        }

        Address a = w.getAddress();
        if (a != null) {
            dto.address = a.getAddress();
            dto.addressCity = a.getCity();
        }

        if (w.getAppliances() != null && !w.getAppliances().isEmpty()) {
            Appliance ap = w.getAppliances().iterator().next();

            if (ap.getApplianceType() != null) {
                dto.applianceType = ap.getApplianceType().getName();
            }
            if (ap.getBrand() != null) {
                dto.applianceBrand = ap.getBrand().getName();
            }
        }

        if (w.getAssignedUser() != null) {
            dto.assignedUserName = w.getAssignedUser().getName();
        }

        return dto;
    }

    public WorkOrder techUpdate(WorkOrder w, WorkOrderTechUpdateRequestDto dto) {

        if (dto.status != null) {
            w.setStatus(dto.status);
        }
        if (dto.workPerformed !=null) {
            w.setWorkPerformed(dto.workPerformed);
        }
        if (dto.notes != null) {
            w.setNotes(dto.notes);
        }
        if (dto.billTo != null) {
            w.setBillTo(dto.billTo);
        }

        return w;
    }

}
