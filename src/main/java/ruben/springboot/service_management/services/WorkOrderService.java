package ruben.springboot.service_management.services;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ruben.springboot.service_management.authentication.SecurityUtils;
import ruben.springboot.service_management.errors.BadRequestException;
import ruben.springboot.service_management.errors.NotFoundException;
import ruben.springboot.service_management.models.WorkOrder;
import ruben.springboot.service_management.models.dtos.lists.WorkOrderListDto;
import ruben.springboot.service_management.models.dtos.requests.WorkOrderFullRequestDto;
import ruben.springboot.service_management.models.dtos.requests.WorkOrderTechUpdateRequestDto;
import ruben.springboot.service_management.models.dtos.responses.WorkOrderResponseDto;
import ruben.springboot.service_management.models.enums.WorkOrderStatus;
import ruben.springboot.service_management.models.mappers.WorkOrderMapper;
import ruben.springboot.service_management.repositories.WorkOrderRepository;

@Service
public class WorkOrderService {

    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Autowired
    private WorkOrderMapper workOrderMapper;

    @Autowired
    private WorkOrderFactory workOrderFactory;

    private static final int MAX_PAGE_SIZE = 30;

    // ADMIN
    @Transactional
    public WorkOrderResponseDto createFull(WorkOrderFullRequestDto req) {

        WorkOrder w = new WorkOrder();
        w = workOrderFactory.buildFromFullRequest(req, w);

        WorkOrder saved = workOrderRepository.save(w);
        return workOrderMapper.toResponse(saved);
    }

    // ADMIN
    @Transactional
    public WorkOrderResponseDto updateFull(Long workOrderId, WorkOrderFullRequestDto req) {

        WorkOrder w = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new NotFoundException("Aviso", workOrderId));

        w = workOrderFactory.buildFromFullRequest(req, w);

        WorkOrder saved = workOrderRepository.save(w);
        return workOrderMapper.toResponse(saved);
    }

    // ADMIN
    @Transactional
    public void delete(Long id) {
        WorkOrder workOrder = workOrderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Aviso", id));
        workOrderRepository.delete(workOrder);
    }

    // ADMIN
    @Transactional(readOnly = true)
    public WorkOrderResponseDto getById(Long id) {

        Optional<WorkOrder> optWorkOrder = workOrderRepository.findById(id);

        return workOrderMapper.toResponse(
                optWorkOrder.orElseThrow(() -> new NotFoundException("Aviso", id)));
    }

    // ADMIN
    @Transactional(readOnly = true)
    public Page<WorkOrderListDto> adminListAll(int pageInt) {

        Page<WorkOrder> page = workOrderRepository.findAll(pageableByPagePriorityDescCreatedDesc(pageInt));

        return page.map(WorkOrderMapper::toList);

    }

    // ADMIN
    @Transactional(readOnly = true)
    public Page<WorkOrderListDto> adminListByScheduledDate(LocalDate date, int pageInt) {

        Page<WorkOrder> page = workOrderRepository.findByScheduledAt(date,
                pageableByPagePriorityDescCreatedAsc(pageInt));

        return page.map(WorkOrderMapper::toList);
    }

    // ADMIN
    @Transactional(readOnly = true)
    public Page<WorkOrderListDto> adminListPending(int pageInt) {

        EnumSet<WorkOrderStatus> pending = EnumSet.of(
                WorkOrderStatus.PENDING_PART,
                WorkOrderStatus.PENDING_CUSTOMER,
                WorkOrderStatus.PENDING_PAYMENT);

        Page<WorkOrder> page = workOrderRepository.findByStatusIn(pending,
                pageableByPagePriorityDescCreatedAsc(pageInt));

        return page.map(WorkOrderMapper::toList);
    }

    // ADMIN
    @Transactional(readOnly = true)
    public Page<WorkOrderListDto> adminListByUserAndScheduledDate(LocalDate date, int pageInt) {

        Long userId = SecurityUtils.currentUserId();

        Page<WorkOrder> page = workOrderRepository.findByAssignedUserIdAndScheduledAt(userId, date,
                pageableByPagePriorityDescCreatedAsc(pageInt));

        return page.map(WorkOrderMapper::toList);
    }

    // ADMIN
    @Transactional(readOnly = true)
    public Page<WorkOrderListDto> adminListByCreationDate(LocalDate date, int pageInt) {
        
        Page<WorkOrder> page = workOrderRepository.findByCreatedAtBetween(
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay().minusNanos(1),
                pageableByPagePriorityDescCreatedAsc(pageInt));

        return page.map(WorkOrderMapper::toList);
    }

    // TECH
    @Transactional
    public WorkOrderResponseDto techUpdate(Long workOrderId, WorkOrderTechUpdateRequestDto req) {

        Long userId = SecurityUtils.currentUserId();

        WorkOrder w = workOrderRepository.findByIdAndAssignedUserId(workOrderId, userId)
                .orElseThrow(() -> new NotFoundException("Aviso", workOrderId));

        w = workOrderMapper.techUpdate(w, req);

        WorkOrder saved = workOrderRepository.save(w);
        return workOrderMapper.toTechResponse(saved);
    }

     // TECH
    @Transactional(readOnly = true)
    public WorkOrderResponseDto techGetById(Long id) {

        Long userId = SecurityUtils.currentUserId();

        WorkOrder workOrder = workOrderRepository.findByIdAndAssignedUserId(id, userId)
                .orElseThrow(() -> new NotFoundException("Aviso", id));

        return workOrderMapper.toTechResponse(workOrder);
    }

    // TECH
    @Transactional(readOnly = true)
    public Page<WorkOrderListDto> techListByUserAndScheduledDate(LocalDate date, int pageInt) {

        Long userId = SecurityUtils.currentUserId();

        LocalDate minDate = LocalDate.now().minusDays(3);
        if (date.isBefore(minDate)) {
            throw new BadRequestException("Fecha", "debe estar dentro de los últimos 3 días");
        }

        Page<WorkOrder> page = workOrderRepository.findByAssignedUserIdAndScheduledAt(userId, date,
                pageableByPagePriorityDescCreatedDesc(pageInt));

        return page.map(WorkOrderMapper::toList);
    }

    // HELPER
    private Pageable pageableByPagePriorityDescCreatedDesc(int page) {
        return PageRequest.of(
                Math.max(page, 0),
                MAX_PAGE_SIZE,
                Sort.by(Sort.Order.desc("priority"), Sort.Order.desc("createdAt")));
    }

    // HELPER
    private Pageable pageableByPagePriorityDescCreatedAsc(int page) {
        return PageRequest.of(
                Math.max(page, 0),
                MAX_PAGE_SIZE,
                Sort.by(Sort.Order.desc("priority"), Sort.Order.asc("createdAt")));
    }

}
