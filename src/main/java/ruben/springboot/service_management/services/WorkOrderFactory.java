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

        // 1) Client
        Client client = clientService.resolve(req.clientId, req.clientDto);

        // 2) Address
        Address address = addressService.resolve(req.addressId, req.addressDto, client);

        // 3) Appliances
        Set<Appliance> appliances = applianceService.resolve(req.applianceIds, req.newAppliances, address);

        // 4) Core
        w.setClient(client);
        w.setAddress(address);

        w.getAppliances().clear();
        w.getAppliances().addAll(appliances);

        Long assignedUserId = req.workOrderDto.assignedUserId;
        w.setAssignedUser(assignedUserId == null ? null : userRepository.findById(assignedUserId).orElseThrow(() -> new NotFoundException("AssignedUser not found")));

        w.setIssueDescription(req.workOrderDto.issueDescription);
        w.setStatus(req.workOrderDto.status == null ? WorkOrderStatus.NEW : req.workOrderDto.status);
        w.setPriority(req.workOrderDto.priority);
        w.setNotes(req.workOrderDto.notes);
        w.setWorkPerformed(req.workOrderDto.workPerformed);
        w.setDiscountVisit(req.workOrderDto.discountVisit != null ? req.workOrderDto.discountVisit : false);
        w.setBillTo(req.workOrderDto.billTo);
        w.setScheduledAt(req.workOrderDto.scheduledAt);

        if (req.tenantDto == null && req.workOrderDto.tenantId == null) {
            w.setTenant(null);
        } else {
            w.setTenant(clientService.resolve(req.workOrderDto.tenantId, req.tenantDto));
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
