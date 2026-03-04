package ruben.springboot.service_management.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ruben.springboot.service_management.authentication.SecurityUtils;
import ruben.springboot.service_management.errors.NotFoundException;
import ruben.springboot.service_management.errors.UnauthorizedException;
import ruben.springboot.service_management.models.WorkOrder;
import ruben.springboot.service_management.models.WorkOrderCharge;
import ruben.springboot.service_management.models.dtos.lists.WorkOrderChargeListDto;
import ruben.springboot.service_management.models.dtos.requests.WorkOrderChargeRequestDto;
import ruben.springboot.service_management.models.dtos.responses.WorkOrderChargeResponseDto;
import ruben.springboot.service_management.models.enums.ChargeType;
import ruben.springboot.service_management.models.mappers.WorkOrderChargeMapper;
import ruben.springboot.service_management.repositories.WorkOrderChargeRepository;
import ruben.springboot.service_management.repositories.WorkOrderRepository;

@Service
public class WorkOrderChargeService {

    @Autowired
    private WorkOrderChargeRepository repository;

    @Autowired
    private WorkOrderRepository workOrderRepository;

    private static final int MAX_PAGE_SIZE = 30;

    @Transactional
    public WorkOrderChargeResponseDto create(Long workOrderId, WorkOrderChargeRequestDto dto) {

        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new NotFoundException("WorkOrder not found: " + workOrderId));

        validateTechAccessToWorkOrder(workOrder);

        WorkOrderCharge newCharge = WorkOrderChargeMapper.toEntity(dto);
        workOrder.addCharge(newCharge);

        recalculateTotalPrice(workOrder);
        WorkOrder savedWorkOrder = workOrderRepository.save(workOrder);

        WorkOrderCharge savedCharge = savedWorkOrder.getCharges().stream()
                .max((a, b) -> Long.compare(a.getId(), b.getId()))
                .orElseThrow(() -> new IllegalStateException("charge was not persisted"));

        return WorkOrderChargeMapper.toResponse(savedCharge);
    }

    @Transactional
    public WorkOrderChargeResponseDto update(Long chargeId, WorkOrderChargeRequestDto dto) {

        WorkOrderCharge chargeDb = repository.findById(chargeId)
                .orElseThrow(() -> new NotFoundException("WorkOrderCharge not found: " + chargeId));

        validateTechAccessToCharge(chargeDb);

        WorkOrderChargeMapper.update(dto, chargeDb);

        WorkOrder workOrder = chargeDb.getWorkOrder();
        recalculateTotalPrice(workOrder);
        workOrderRepository.save(workOrder);

        return WorkOrderChargeMapper.toResponse(chargeDb);
    }

    @Transactional
    public void delete(Long chargeId) {

        WorkOrderCharge chargeDb = repository.findById(chargeId)
                .orElseThrow(() -> new NotFoundException("WorkOrderCharge not found: " + chargeId));

        WorkOrder workOrder = chargeDb.getWorkOrder();
        workOrder.removeCharge(chargeDb);

        recalculateTotalPrice(workOrder);
        workOrderRepository.save(workOrder);
    }

    @Transactional(readOnly = true)
    public WorkOrderChargeResponseDto getById(Long chargeId) {
        WorkOrderCharge charge = repository.findById(chargeId)
                .orElseThrow(() -> new NotFoundException("WorkOrderCharge not found: " + chargeId));

        validateTechAccessToCharge(charge);

        return WorkOrderChargeMapper.toResponse(charge);
    }

    @Transactional(readOnly = true)
    public Page<WorkOrderChargeListDto> listAll(int pageInt) {
        return repository.findAll(pageableByCreatedAtAsc(pageInt)).map(WorkOrderChargeMapper::toList);
    }

    @Transactional(readOnly = true)
    public List<WorkOrderChargeListDto> listByWorkOrderId(Long workOrderId) {
        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new NotFoundException("WorkOrder not found: " + workOrderId));

        validateTechAccessToWorkOrder(workOrder);

        List<WorkOrderCharge> charges = workOrder.getCharges();
        return charges.stream().map(WorkOrderChargeMapper::toList).toList();
    }

    // HELPER
    private void validateTechAccessToCharge(WorkOrderCharge workOrderCharge) {
        if (!currentUserIsTech()) {
            return;
        }

        Long currentUserId = SecurityUtils.currentUserId();
        Long createdUserId = workOrderCharge.getCreatedUserId();

        if (currentUserId == null || createdUserId == null || !createdUserId.equals(currentUserId)) {
            throw new UnauthorizedException("Technician is not allowed to access this charge");
        }
    }

    // HELPER
    private void validateTechAccessToWorkOrder(WorkOrder workOrder) {
        if (!currentUserIsTech()) {
            return;
        }

        Long currentUserId = SecurityUtils.currentUserId();
        Long assignedUserId = workOrder.getAssignedUser() != null ? workOrder.getAssignedUser().getId() : null;

        if (currentUserId == null || assignedUserId == null || !assignedUserId.equals(currentUserId)) {
            throw new UnauthorizedException("Technician is not allowed to access this work order");
        }
    }

    // HELPER
    private boolean currentUserIsTech() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return false;
        }
        return auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch("ROLE_TECH"::equals);
    }

    // HELPER
    private void recalculateTotalPrice(WorkOrder workOrder) {

        boolean visitChargedAndPaid = false;
        BigDecimal visitPrice = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;

        for (WorkOrderCharge charge : workOrder.getCharges()) {
            if (charge.getPrice() != null) {
                total = total.add(charge.getPrice());
            }

            if (charge.getChargeType() == ChargeType.VISIT && charge.getPaid() && charge.getPrice() != null
                    && charge.getPrice().compareTo(BigDecimal.ZERO) > 0) {
                visitChargedAndPaid = true;
                visitPrice = visitPrice.add(charge.getPrice());
            }
        }

        if (Boolean.TRUE.equals(workOrder.getDiscountVisit()) && visitChargedAndPaid) {
            workOrder.setTotalPrice(total.subtract(visitPrice));
        }

        workOrder.setTotalPrice(total);
    }

    // HELPER
    private Pageable pageableByCreatedAtAsc(int pageInt) {
        return PageRequest.of(
                Math.max(pageInt, 0),
                MAX_PAGE_SIZE,
                Sort.by(Sort.Order.asc("createdAt"), Sort.Order.asc("id")));
    }

}