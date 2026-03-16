package ruben.springboot.service_management.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ruben.springboot.service_management.authentication.SecurityUtils;
import ruben.springboot.service_management.errors.NotFoundException;
import ruben.springboot.service_management.models.Address;
import ruben.springboot.service_management.models.Appliance;
import ruben.springboot.service_management.models.Client;
import ruben.springboot.service_management.models.User;
import ruben.springboot.service_management.models.WorkOrder;
import ruben.springboot.service_management.models.WorkOrderCharge;
import ruben.springboot.service_management.models.dtos.requests.WorkOrderChargeRequestDto;
import ruben.springboot.service_management.models.dtos.requests.WorkOrderFullRequestDto;
import ruben.springboot.service_management.models.enums.ChargeType;
import ruben.springboot.service_management.models.enums.WorkOrderPriority;
import ruben.springboot.service_management.models.enums.WorkOrderStatus;
import ruben.springboot.service_management.models.mappers.WorkOrderChargeMapper;
import ruben.springboot.service_management.repositories.UserRepository;

@Service
public class WorkOrderFactory {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ApplianceService applianceService;


    public WorkOrder buildFromFullRequest(WorkOrderFullRequestDto req, WorkOrder w) {

        Long currentUserId = SecurityUtils.currentUserId();
        User currentUser = userRepository.getReferenceById(currentUserId);

        Client client = clientService.resolveForWorkOrder(req.clientId, req.client);

        Address address = addressService.resolve(req.addressId, req.address, client);

        Set<Appliance> appliances = applianceService.resolve(req.applianceIds, req.newAppliances, address);

        w.setClient(client);
        w.setAddress(address);
        w.getAppliances().clear();
        w.getAppliances().addAll(appliances);

        Long assignedUserId = req.workOrder.assignedUserId;
        w.setAssignedUser(assignedUserId == null ? null : userRepository.findById(assignedUserId)
                .orElseThrow(() -> new NotFoundException("Usuario asignado", assignedUserId)));

        w.setIssueDescription(req.workOrder.issueDescription);
        w.setStatus(req.workOrder.status == null ? WorkOrderStatus.NEW : req.workOrder.status);
        w.setPriority(req.workOrder.priority == null ? WorkOrderPriority.MID.getPriority() : req.workOrder.priority);
        w.setNotes(req.workOrder.notes);
        w.setWorkPerformed(req.workOrder.workPerformed);
        w.setDiscountVisit(req.workOrder.discountVisit != null ? req.workOrder.discountVisit : false);
        w.setBillTo(req.workOrder.billTo);
        w.setCreatedAt(req.workOrder.createdAt != null ? req.workOrder.createdAt : req.workOrder.lastUpdateAt);
        w.setLastUpdatedAt(req.workOrder.lastUpdateAt);
        w.setScheduledAt(req.workOrder.scheduledAt);

        if (req.tenant == null && req.tenantId == null) {
            w.setTenant(null);
        } else {
            w.setTenant(clientService.resolveForWorkOrder(req.tenantId, req.tenant));
        }

        if (w.getId() == null) {
            w.setCreatedUser(currentUser);
        }

        w.setLastUpdatedUser(currentUser);

        if ((w.getStatus() == WorkOrderStatus.CLOSED || w.getStatus() == WorkOrderStatus.APPLIANCE_INSTALLED)
                && w.getClosedAt() == null) {
            w.setClosedAt(LocalDateTime.now());
            w.setClosedUser(currentUser);
        }

        recalcChargesAndTotal(w, req.charges);

        return w;
    }

    private void recalcChargesAndTotal(WorkOrder w, List<WorkOrderChargeRequestDto> chargesDto) {

        w.getCharges().clear();

        boolean visitChargedAndPaid = false;
        BigDecimal visitPrice = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;

        if (chargesDto != null) {
            for (WorkOrderChargeRequestDto cDto : chargesDto) {
                WorkOrderCharge ch = WorkOrderChargeMapper.toEntity(cDto);
                w.addCharge(ch);

                if (ch.getPrice() != null) {
                    total = total.add(ch.getPrice());
                }

                if (ch.getChargeType() == ChargeType.VISIT && ch.getPaid() && ch.getPrice() != null && ch.getPrice().compareTo(BigDecimal.ZERO) > 0) {
                    visitChargedAndPaid = true;
                    visitPrice = visitPrice.add(ch.getPrice());
                }
            }
        }

        if (w.getDiscountVisit() && visitChargedAndPaid) {
            w.setTotalPrice(total.subtract(visitPrice));
        } else {
            w.setTotalPrice(total);
        }
    }
}
