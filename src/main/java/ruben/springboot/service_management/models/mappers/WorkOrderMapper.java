package ruben.springboot.service_management.models.mappers;

import ruben.springboot.service_management.errors.NotFoundException;
import ruben.springboot.service_management.models.*;
import ruben.springboot.service_management.models.dtos.lists.WorkOrderListDto;
import ruben.springboot.service_management.models.dtos.requests.WorkOrderChargeRequestDto;
import ruben.springboot.service_management.models.dtos.requests.WorkOrderRequestDto;
import ruben.springboot.service_management.models.dtos.responses.WorkOrderResponseDto;
import ruben.springboot.service_management.models.enums.ChargeType;
import ruben.springboot.service_management.models.enums.WorkOrderPriority;
import ruben.springboot.service_management.models.enums.WorkOrderStatus;
import ruben.springboot.service_management.repositories.AddressRepository;
import ruben.springboot.service_management.repositories.ApplianceRepository;
import ruben.springboot.service_management.repositories.ClientRepository;
import ruben.springboot.service_management.repositories.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkOrderMapper {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ApplianceRepository applianceRepository;
    @Autowired
    private WorkOrderChargeMapper chargeMapper;
    @Autowired
    private UserRepository userRepository;

    public WorkOrder toEntity(WorkOrderRequestDto dto, Long currentUserId) {

        WorkOrder w = new WorkOrder();
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new NotFoundException("Current user not found: " + currentUserId));

        w.setClient(clientRepository.findById(dto.clientId)
                .orElseThrow(() -> new NotFoundException("Client not found: " + dto.clientId)));

        w.setAddress(addressRepository.findById(dto.addressId)
                .orElseThrow(() -> new NotFoundException("Address not found: " + dto.addressId)));

        w.setAssignedUser(dto.assignedUserId == null ? null
                : userRepository.findById(dto.assignedUserId)
                        .orElseThrow(() -> new NotFoundException("AssignedUser not found: " + dto.assignedUserId)));

        w.setCreatedUser(currentUser);
        w.setLastUpdatedUser(currentUser);

        w.setIssueDescription(dto.issueDescription);
        w.setStatus(dto.status == null ? WorkOrderStatus.NEW : dto.status);
        w.setPriority(dto.priority == null ? WorkOrderPriority.MEDIUM : dto.priority);
        w.setNotes(dto.notes);
        w.setWorkPerformed(dto.workPerformed);

        if ((dto.status == WorkOrderStatus.CLOSED || dto.status == WorkOrderStatus.APPLIANCE_INSTALLED)
                && w.getClosedAt() == null)

        {
            w.setClosedAt(LocalDateTime.now());
        }

        boolean visitChargedAndPaid = false;
        BigDecimal visitPrice = new BigDecimal(0);
        BigDecimal acumulativePrice = new BigDecimal(0);
        if (dto.charges != null) {
            for (WorkOrderChargeRequestDto woc : dto.charges) {
                WorkOrderCharge charge = chargeMapper.toEntity(woc, currentUserId);
                w.addCharge(charge);

                acumulativePrice.add(charge.getPrice());

                if (charge.getChargeType() == ChargeType.VISIT && charge.getPaid() == true
                        && charge.getPrice().compareTo(BigDecimal.ZERO) > 0) {
                    visitChargedAndPaid = true;
                    visitPrice.add(charge.getPrice());
                }
            }
        }

        w.setDiscountVisit(dto.discountVisit == null ? false : dto.discountVisit);
        w.setBillTo(dto.billTo);

        if (dto.discountVisit == true && visitChargedAndPaid == true) {
            w.setTotalPrice(acumulativePrice.subtract(visitPrice));
        } else {
            w.setTotalPrice(acumulativePrice);
        }

        w.setScheduledAt(dto.sheduledAt);
        w.setTenant(dto.tenantId == null ? null
                : clientRepository.findById(dto.tenantId)
                        .orElseThrow(() -> new NotFoundException("Tenant not found: " + dto.tenantId)));

        if (dto.applianceIds != null && !dto.applianceIds.isEmpty()) {

            List<Appliance> appliances = applianceRepository.findAllById(dto.applianceIds);
            w.getAppliances().clear();
            w.getAppliances().addAll(appliances);

            if (appliances.size() != dto.applianceIds.size()) {
                throw new NotFoundException("Some applianceIds not found");
            }
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
