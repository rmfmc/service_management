package ruben.springboot.service_management.models.mappers;

import ruben.springboot.service_management.errors.NotFoundException;
import ruben.springboot.service_management.models.*;
import ruben.springboot.service_management.models.dtos.lists.WorkOrderListDto;
import ruben.springboot.service_management.models.dtos.requests.WorkOrderChargeRequestDto;
import ruben.springboot.service_management.models.dtos.requests.WorkOrderRequestDto;
import ruben.springboot.service_management.models.dtos.responses.ApplianceResponseDto;
import ruben.springboot.service_management.models.dtos.responses.WorkOrderChargeResponseDto;
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
import java.util.ArrayList;
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
        User currentUser = userRepository.getReferenceById(currentUserId);

        w.setClient(clientRepository.getReferenceById(dto.clientId));

        w.setAddress(addressRepository.getReferenceById(dto.addressId));

        w.setAssignedUser(dto.assignedUserId == null ? null : userRepository.getReferenceById(dto.assignedUserId));

        w.setCreatedUser(currentUser);
        w.setLastUpdatedUser(currentUser);

        w.setIssueDescription(dto.issueDescription);
        w.setStatus(dto.status == null ? WorkOrderStatus.NEW : dto.status);
        w.setPriority(dto.priority == null ? WorkOrderPriority.MEDIUM : dto.priority);
        w.setNotes(dto.notes);
        w.setWorkPerformed(dto.workPerformed);

        if ((w.getStatus() == WorkOrderStatus.CLOSED || w.getStatus() == WorkOrderStatus.APPLIANCE_INSTALLED)
                && w.getClosedAt() == null) {
            w.setClosedAt(LocalDateTime.now());
            w.setClosedUser(currentUser);
        }

        boolean visitChargedAndPaid = false;
        BigDecimal visitPrice = BigDecimal.ZERO;
        BigDecimal acumulativePrice = BigDecimal.ZERO;
        if (dto.charges != null) {
            for (WorkOrderChargeRequestDto woc : dto.charges) {
                WorkOrderCharge charge = chargeMapper.toEntity(woc, currentUserId);
                w.addCharge(charge);

                acumulativePrice = acumulativePrice.add(charge.getPrice());

                if (charge.getChargeType() == ChargeType.VISIT
                        && charge.getPaid() == true
                        && charge.getPrice().compareTo(BigDecimal.ZERO) > 0) {
                    visitChargedAndPaid = true;
                    visitPrice = visitPrice.add(charge.getPrice());
                }
            }
        }

        w.setDiscountVisit(dto.discountVisit == null ? false : dto.discountVisit);
        w.setBillTo(dto.billTo);

        if (w.getDiscountVisit() == true && visitChargedAndPaid) {
            w.setTotalPrice(acumulativePrice.subtract(visitPrice));
        } else {
            w.setTotalPrice(acumulativePrice);
        }

        w.setScheduledAt(dto.scheduledAt);
        w.setTenant(dto.tenantId == null ? null : clientRepository.getReferenceById(dto.tenantId));

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

    public WorkOrder update(WorkOrderRequestDto dto, WorkOrder w, Long currentUserId) {

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new NotFoundException("Current user not found: " + currentUserId));

        w.setClient(clientRepository.findById(dto.clientId)
                .orElseThrow(() -> new NotFoundException("Client not found: " + dto.clientId)));

        w.setAddress(addressRepository.findById(dto.addressId)
                .orElseThrow(() -> new NotFoundException("Address not found: " + dto.addressId)));

        w.setAssignedUser(dto.assignedUserId == null ? null
                : userRepository.findById(dto.assignedUserId)
                        .orElseThrow(() -> new NotFoundException("AssignedUser not found: " + dto.assignedUserId)));

        w.setLastUpdatedUser(currentUser);

        w.setIssueDescription(dto.issueDescription);
        w.setStatus(dto.status == null ? WorkOrderStatus.NEW : dto.status);
        w.setPriority(dto.priority == null ? WorkOrderPriority.MEDIUM : dto.priority);
        w.setNotes(dto.notes);
        w.setWorkPerformed(dto.workPerformed);

        if ((w.getStatus() == WorkOrderStatus.CLOSED || w.getStatus() == WorkOrderStatus.APPLIANCE_INSTALLED)
                && w.getClosedAt() == null) {
            w.setClosedAt(LocalDateTime.now());
            w.setClosedUser(currentUser);
        }

        w.getCharges().clear();

        boolean visitChargedAndPaid = false;
        BigDecimal visitPrice = BigDecimal.ZERO;
        BigDecimal acumulativePrice = BigDecimal.ZERO;

        if (dto.charges != null) {
            for (WorkOrderChargeRequestDto wocDto : dto.charges) {
                WorkOrderCharge charge = chargeMapper.toEntity(wocDto, currentUserId);
                w.addCharge(charge);

                if (charge.getPrice() != null) {
                    acumulativePrice = acumulativePrice.add(charge.getPrice());
                }

                if (charge.getChargeType() == ChargeType.VISIT
                        && Boolean.TRUE.equals(charge.getPaid())
                        && charge.getPrice() != null
                        && charge.getPrice().compareTo(BigDecimal.ZERO) > 0) {

                    visitChargedAndPaid = true;
                    visitPrice = visitPrice.add(charge.getPrice());
                }
            }
        }

        w.setDiscountVisit(dto.discountVisit == null ? false : dto.discountVisit);
        w.setBillTo(dto.billTo);

        if (w.getDiscountVisit() == true && visitChargedAndPaid) {
            w.setTotalPrice(acumulativePrice.subtract(visitPrice));
        } else {
            w.setTotalPrice(acumulativePrice);
        }

        w.setScheduledAt(dto.scheduledAt);

        w.setTenant(dto.tenantId == null ? null
                : clientRepository.findById(dto.tenantId)
                        .orElseThrow(() -> new NotFoundException("Tenant not found: " + dto.tenantId)));

        w.getAppliances().clear();
        if (dto.applianceIds != null && !dto.applianceIds.isEmpty()) {
            List<Appliance> appliances = applianceRepository.findAllById(dto.applianceIds);

            if (appliances.size() != dto.applianceIds.size()) {
                throw new NotFoundException("Some applianceIds not found");
            }
            w.getAppliances().addAll(appliances);
        }

        return w;
    }

    public WorkOrderResponseDto toResponse(WorkOrder w) {

        WorkOrderResponseDto dto = new WorkOrderResponseDto();

        dto.workOrderId = w.getId();

        // CLIENT
        Client client = w.getClient();
        if (client != null) {
            dto.clientId = client.getId();
            dto.clientName = client.getName();
            dto.clientPhone = client.getPhone();
        }

        // ADDRESS (real)
        Address address = w.getAddress();
        if (address != null) {
            dto.addressId = address.getId();
            dto.address = address.getAddress();
            dto.city = address.getCity();
            dto.province = address.getProvince();
            dto.postalCode = address.getPostalCode();
        }

        // TENANT (opcional)
        Client tenant = w.getTenant();
        if (tenant != null) {
            dto.tenantId = tenant.getId();
            dto.tenantName = tenant.getName();
            dto.tenantPhone = tenant.getPhone();
        }

        // USERS (resumen)
        if (w.getAssignedUser() != null) {
            dto.assignedUser = w.getAssignedUser().getName();
        }

        if (w.getCreatedUser() != null) {
            dto.createdUser = w.getCreatedUser().getName();
        }

        if (w.getLastUpdatedUser() != null) {
            dto.lastUpdatedUser = w.getLastUpdatedUser().getName();
        }

        // CAMPOS DEL AVISO
        dto.issueDescription = w.getIssueDescription();

        dto.status = w.getStatus().getLabelEs();
        dto.priority = w.getPriority().getLabelEs();

        dto.notes = w.getNotes();
        dto.workPerformed = w.getWorkPerformed();

        dto.discountVisit = w.getDiscountVisit();
        dto.billTo = w.getBillTo();
        dto.totalPrice = w.getTotalPrice();

        dto.scheduledAt = w.getScheduledAt();

        dto.createdAt = w.getCreatedAt();
        dto.closedAt = w.getClosedAt();
        dto.lastUpdatedAt = w.getLastUpdatedAt();

        // APPLIANCES (many-to-many)
        dto.appliances = new ArrayList<>();
        if (w.getAppliances() != null) {
            for (Appliance a : w.getAppliances()) {
                ApplianceResponseDto ad = new ApplianceResponseDto();
                ad.id = a.getId();

                if (a.getApplianceType() != null) {
                    ad.id = a.getApplianceType().getId();
                    ad.applianceTypeName = a.getApplianceType().getName();
                }

                if (a.getBrand() != null) {
                    ad.brandId = a.getBrand().getId();
                    ad.brandName = a.getBrand().getName();
                }

                ad.model = a.getModel();
                ad.serialNumber = a.getSerialNumber();
                ad.active = a.isActive();

                dto.appliances.add(ad);
            }
        }

        // CHARGES (1-to-many, ya ordenados por @OrderBy(createdAt ASC))
        dto.charges = new ArrayList<>();
        if (w.getCharges() != null) {
            for (WorkOrderCharge ch : w.getCharges()) {
                WorkOrderChargeResponseDto cd = new WorkOrderChargeResponseDto();
                cd.id = ch.getId();
                cd.workOrderId = w.getId();

                cd.chargeType = ch.getChargeType().getLabelEs();
                cd.paymentMethod = ch.getPaymentMethod().getLabelEs();

                cd.description = ch.getDescription();
                cd.price = ch.getPrice();
                cd.payer = ch.getPayer();
                cd.paid = ch.getPaid();

                cd.createdUserId = ch.getCreatedUserId();
                cd.createdAt = ch.getCreatedAt();

                dto.charges.add(cd);
            }
        }

        return dto;
    }

    public WorkOrderListDto toList(WorkOrder w) {

        WorkOrderListDto dto = new WorkOrderListDto();

        dto.id = w.getId();
        dto.issueDescription = w.getIssueDescription();

        if (w.getStatus() != null)
            dto.status = w.getStatus().getLabelEs();
        if (w.getPriority() != null) {
            dto.priority = w.getPriority().getLabelEs();
            dto.priorityInt = w.getPriority().getPriorityInt();
        }

        dto.scheduledAt = w.getScheduledAt();
        dto.createdAt = w.getCreatedAt();

        // CLIENT
        Client c = w.getClient();
        if (c != null) {
            dto.clientName = c.getName();
            dto.clientPhone = c.getPhone();
        }

        // ADDRESS (real)
        Address a = w.getAddress();
        if (a != null) {
            dto.clientAddress = a.getAddress();
            dto.clientCity = a.getCity();
        }

        // APPLIANCE (listado -> primero)
        if (w.getAppliances() != null && !w.getAppliances().isEmpty()) {
            Appliance ap = w.getAppliances().iterator().next();

            if (ap.getApplianceType() != null) {
                dto.applianceType = ap.getApplianceType().getName(); // o labelEs si lo tienes
            }
            if (ap.getBrand() != null) {
                dto.applianceBrand = ap.getBrand().getName();
            }
        }

        // ASSIGNED USER
        if (w.getAssignedUser() != null) {
            dto.assignedUserName = w.getAssignedUser().getName();
        }

        return dto;
    }

}
