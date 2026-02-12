package ruben.springboot.service_management.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ruben.springboot.service_management.authentication.SecurityUtils;
import ruben.springboot.service_management.errors.NotFoundException;
import ruben.springboot.service_management.models.Address;
import ruben.springboot.service_management.models.Appliance;
import ruben.springboot.service_management.models.Client;
import ruben.springboot.service_management.models.User;
import ruben.springboot.service_management.models.WorkOrder;
import ruben.springboot.service_management.models.WorkOrderCharge;
import ruben.springboot.service_management.models.dtos.lists.WorkOrderListDto;
import ruben.springboot.service_management.models.dtos.requests.WorkOrderChargeRequestDto;
import ruben.springboot.service_management.models.dtos.requests.WorkOrderFullRequestDto;
import ruben.springboot.service_management.models.dtos.responses.WorkOrderResponseDto;
import ruben.springboot.service_management.models.enums.ChargeType;
import ruben.springboot.service_management.models.enums.WorkOrderPriority;
import ruben.springboot.service_management.models.enums.WorkOrderStatus;
import ruben.springboot.service_management.models.mappers.WorkOrderChargeMapper;
import ruben.springboot.service_management.models.mappers.WorkOrderMapper;
import ruben.springboot.service_management.repositories.ApplianceRepository;
import ruben.springboot.service_management.repositories.ClientRepository;
import ruben.springboot.service_management.repositories.UserRepository;
import ruben.springboot.service_management.repositories.WorkOrderRepository;

@Service
public class WorkOrderService {

    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplianceRepository applianceRepository;

    @Autowired
    private ApplianceService applianceService;

    @Autowired
    private WorkOrderMapper workOrderMapper;

    @Autowired
    private WorkOrderFactory workOrderFactory;

    @Transactional
    public WorkOrderResponseDto createFull(WorkOrderFullRequestDto req) {

        if (req == null) {
            throw new IllegalArgumentException("request is required");
        }

        WorkOrder w = new WorkOrder();
        w = workOrderFactory.buildFromFullRequest(req, w);

        WorkOrder saved = workOrderRepository.save(w);
        return workOrderMapper.toResponse(saved);
    }

    @Transactional
    public WorkOrderResponseDto updateFull(Long workOrderId, WorkOrderFullRequestDto req) {
        
        if (req == null){
            throw new IllegalArgumentException("request is required");
        }

        WorkOrder w = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new NotFoundException("WorkOrder not found: " + workOrderId));

        w = workOrderFactory.buildFromFullRequest(req, w);

        WorkOrder saved = workOrderRepository.save(w);
        return workOrderMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<WorkOrderListDto> list(Long clientId, Long assignedUserId, String status) {

        List<WorkOrderListDto> list;

        if (clientId != null)
            list = workOrderRepository.findByClientIdOrderByCreatedAtDesc(clientId).stream()
                    .map(w -> workOrderMapper.toList(w)).toList();
        else if (assignedUserId != null)
            list = workOrderRepository.findByAssignedUserIdOrderByCreatedAtDesc(assignedUserId).stream()
                    .map(w -> workOrderMapper.toList(w)).toList();
        else if (status != null)
            list = workOrderRepository.findByStatusOrderByCreatedAtDesc(status).stream()
                    .map(w -> workOrderMapper.toList(w)).toList();
        else
            list = workOrderRepository.findAllForList();

        return list;
    }

    @Transactional(readOnly = true)
    public WorkOrderResponseDto getById(Long id) {
        WorkOrder w = workOrderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("work order not found"));
        return workOrderMapper.toResponse(w);
    }

    @Transactional
    public void delete(Long id) {
        workOrderRepository.deleteById(id);
    }

}
