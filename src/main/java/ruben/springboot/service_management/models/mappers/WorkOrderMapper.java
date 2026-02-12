package ruben.springboot.service_management.models.mappers;

import ruben.springboot.service_management.authentication.SecurityUtils;
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
    private UserRepository userRepository;

    public WorkOrderResponseDto toResponse(WorkOrder w) {

        WorkOrderResponseDto dto = new WorkOrderResponseDto();

        dto.workOrderId = w.getId();

        Client client = w.getClient();
        if (client != null) {
            dto.clientId = client.getId();
            dto.clientName = client.getName();
            dto.clientPhone = client.getPhone();
        }

        Address address = w.getAddress();
        if (address != null) {
            dto.addressId = address.getId();
            dto.address = address.getAddress();
            dto.city = address.getCity();
            dto.province = address.getProvince();
            dto.postalCode = address.getPostalCode();
        }

        Client tenant = w.getTenant();
        if (tenant != null) {
            dto.tenantId = tenant.getId();
            dto.tenantName = tenant.getName();
            dto.tenantPhone = tenant.getPhone();
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

        Client c = w.getClient();
        if (c != null) {
            dto.clientName = c.getName();
            dto.clientPhone = c.getPhone();
        }

        Address a = w.getAddress();
        if (a != null) {
            dto.clientAddress = a.getAddress();
            dto.clientCity = a.getCity();
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

}
