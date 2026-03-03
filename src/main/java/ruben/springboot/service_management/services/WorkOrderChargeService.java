package ruben.springboot.service_management.services;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ruben.springboot.service_management.errors.NotFoundException;
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

        return WorkOrderChargeMapper.toResponse(charge);
    }

    @Transactional(readOnly = true)
    public Page<WorkOrderChargeListDto> listAll(int pageInt) {
        return repository.findAll(pageableByCreatedAtAsc(pageInt)).map(WorkOrderChargeMapper::toList);
    }

    @Transactional(readOnly = true)
    public List<WorkOrderChargeListDto> listByWorkOrderId(Long workOrderId) {

        if (!workOrderRepository.existsById(workOrderId)) {
            throw new NotFoundException("WorkOrder not found: " + workOrderId);
        }

        List<WorkOrderCharge> charges = repository.findByWorkOrderIdOrderByCreatedAtAsc(workOrderId);
        return charges.stream().map(WorkOrderChargeMapper::toList).toList();
    }

    
    private void recalculateTotalPrice(WorkOrder workOrder) {

        boolean visitChargedAndPaid = false;
        BigDecimal visitPrice = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;
        
        for (WorkOrderCharge charge : workOrder.getCharges()) {
            if (charge.getPrice() != null) {
                total = total.add(charge.getPrice());
            }
            
            if (charge.getChargeType() == ChargeType.VISIT && charge.getPaid() && charge.getPrice() != null && charge.getPrice().compareTo(BigDecimal.ZERO) > 0) {
                visitChargedAndPaid = true;
                visitPrice = visitPrice.add(charge.getPrice());
            }
        }
        
        if (Boolean.TRUE.equals(workOrder.getDiscountVisit()) && visitChargedAndPaid) {
            workOrder.setTotalPrice(total.subtract(visitPrice));
        }
        
        workOrder.setTotalPrice(total);
    }

    private Pageable pageableByCreatedAtAsc(int pageInt) {
        return PageRequest.of(
                Math.max(pageInt, 0),
                MAX_PAGE_SIZE,
                Sort.by(Sort.Order.asc("createdAt"), Sort.Order.asc("id")));
    }

}