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
import ruben.springboot.service_management.errors.NotFoundException;
import ruben.springboot.service_management.errors.UnauthorizedException;
import ruben.springboot.service_management.models.WorkOrder;
import ruben.springboot.service_management.models.dtos.lists.WorkOrderListDto;
import ruben.springboot.service_management.models.dtos.requests.WorkOrderFullRequestDto;
import ruben.springboot.service_management.models.dtos.responses.WorkOrderResponseAdminDto;
import ruben.springboot.service_management.models.dtos.responses.WorkOrderResponseTechDto;
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
    public WorkOrderResponseAdminDto createFull(WorkOrderFullRequestDto req) {

        if (req == null) {
            throw new IllegalArgumentException("request is required");
        }

        WorkOrder w = new WorkOrder();
        w = workOrderFactory.buildFromFullRequest(req, w);

        WorkOrder saved = workOrderRepository.save(w);
        return workOrderMapper.toResponseAdmin(saved);
    }

    // ADMIN
    @Transactional
    public WorkOrderResponseAdminDto updateFull(Long workOrderId, WorkOrderFullRequestDto req) {

        if (req == null) {
            throw new IllegalArgumentException("request is required");
        }

        WorkOrder w = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new NotFoundException("WorkOrder not found: " + workOrderId));

        w = workOrderFactory.buildFromFullRequest(req, w);

        WorkOrder saved = workOrderRepository.save(w);
        return workOrderMapper.toResponseAdmin(saved);
    }

    // ADMIN
    @Transactional
    public void delete(Long id) {
        workOrderRepository.deleteById(id);
    }

    // ADMIN
    @Transactional(readOnly = true)
    public WorkOrderResponseAdminDto adminGetById(Long id) {

        Optional<WorkOrder> optWorkOrder = workOrderRepository.findById(id);

        return workOrderMapper.toResponseAdmin(
                optWorkOrder.orElseThrow(() -> new NotFoundException("workOrder not found with id " + id)));
    }

    // TECH
    @Transactional(readOnly = true)
    public WorkOrderResponseTechDto techGetById(Long id) {

        Optional<WorkOrder> optWorkOrder = workOrderRepository.findById(id);

        return workOrderMapper.toResponseTech(
                optWorkOrder.orElseThrow(() -> new NotFoundException("workOrder not found with id " + id)));
    }

    // ADMIN
    @Transactional(readOnly = true)
    public Page<WorkOrderListDto> adminListAll(int pageInt) {

        Page<WorkOrder> page = workOrderRepository.findAll(pageableByPagePriorityDescCreatedDesc(pageInt));

        return page.map(workOrderMapper::toList);

    }

    // ADMIN
    @Transactional(readOnly = true)
    public Page<WorkOrderListDto> adminListByScheduledDate(LocalDate date, int pageInt) {

        Page<WorkOrder> page = workOrderRepository.findByScheduledAt(date,
                pageableByPagePriorityDescCreatedAsc(pageInt));

        return page.map(workOrderMapper::toList);
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

        return page.map(workOrderMapper::toList);
    }

    // ADMIN
    @Transactional(readOnly = true)
    public Page<WorkOrderListDto> adminListByUserAndScheduledDate(LocalDate date, int pageInt) {

        Long userId = SecurityUtils.currentUserId();

        Page<WorkOrder> page = workOrderRepository.findByAssignedUserIdAndScheduledAt(userId, date,
                pageableByPagePriorityDescCreatedAsc(pageInt));

        return page.map(workOrderMapper::toList);
    }

    @Transactional(readOnly = true)
    public Page<WorkOrderListDto> adminListByCreationDate(LocalDate date, int pageInt) {
        
        Page<WorkOrder> page = workOrderRepository.findByCreatedAt(date, pageableByPagePriorityDescCreatedAsc(pageInt));

        return page.map(workOrderMapper::toList);
    }

    // TECH
    @Transactional(readOnly = true)
    public Page<WorkOrderListDto> techListByUserAndScheduledDate(LocalDate date, int pageInt) {

        Long userId = SecurityUtils.currentUserId();

        LocalDate minDate = LocalDate.now().minusDays(3);
        if (date.isBefore(minDate)) {
            throw new UnauthorizedException("date must be within the last 3 days");
        }

        Page<WorkOrder> page = workOrderRepository.findByAssignedUserIdAndScheduledAt(userId, date,
                pageableByPagePriorityDescCreatedDesc(pageInt));

        return page.map(workOrderMapper::toList);
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
