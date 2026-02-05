package ruben.springboot.service_management.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ruben.springboot.service_management.authentication.SecurityUtils;
import ruben.springboot.service_management.errors.NotFoundException;
import ruben.springboot.service_management.models.Appliance;
import ruben.springboot.service_management.models.Client;
import ruben.springboot.service_management.models.User;
import ruben.springboot.service_management.models.WorkOrder;
import ruben.springboot.service_management.models.dto.WorkOrderListDto;
import ruben.springboot.service_management.models.dto.WorkOrderRequestDto;
import ruben.springboot.service_management.models.dto.WorkOrderResponseDto;
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
    private UserRepository userRepository;

    @Autowired
    private ApplianceRepository applianceRepository;

    @Transactional
    public WorkOrderResponseDto create(WorkOrderRequestDto req) {
        Long currentUserId = SecurityUtils.currentUserId();

        Client client = clientRepository.findById(req.clientId)
                .orElseThrow(() -> new NotFoundException("client not found"));

        Client owner = null;
        if (req.ownerId != null) {
            owner = clientRepository.findById(req.ownerId)
                    .orElseThrow(() -> new NotFoundException("owner not found"));
        }

        Appliance appliance = null;
        if (req.applianceId != null) {
            appliance = applianceRepository.findById(req.applianceId)
                    .orElseThrow(() -> new NotFoundException("appliance not found"));
        }

        User assigned = null;
        if (req.assignedUserId != null) {
            assigned = userRepository.findById(req.assignedUserId)
                    .orElseThrow(() -> new NotFoundException("assigned user not found"));
        }

        User createdUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new NotFoundException("created user not found"));

        // al crear, lastUpdated = created
        User lastUpdatedUser = createdUser;

        WorkOrder w = WorkOrderMapper.toEntity(req, client, owner, appliance, assigned, createdUser, lastUpdatedUser);
        w = workOrderRepository.save(w);
        return WorkOrderMapper.toResponse(w);
    }

    @Transactional(readOnly = true)
    public List<WorkOrderListDto> list(Long clientId, Long assignedUserId, String status) {

        List<WorkOrderListDto> list;

        if (clientId != null)
            list = workOrderRepository.findByClientIdOrderByCreatedAtDesc(clientId).stream().map(w -> WorkOrderMapper.toList(w)).toList();
        else if (assignedUserId != null)
            list = workOrderRepository.findByAssignedUserIdOrderByCreatedAtDesc(assignedUserId).stream().map(w -> WorkOrderMapper.toList(w)).toList();
        else if (status != null)
            list = workOrderRepository.findByStatusOrderByCreatedAtDesc(status).stream().map(w -> WorkOrderMapper.toList(w)).toList();
        else
            //list = workOrderRepository.findAllByOrderByCreatedAtDesc();
            list = workOrderRepository.findAllForList();

        return list;
    }

    @Transactional(readOnly = true)
    public WorkOrderResponseDto getById(Long id) {
        WorkOrder w = workOrderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("work order not found"));
        return WorkOrderMapper.toResponse(w);
    }

    @Transactional
    public WorkOrderResponseDto update(Long id, WorkOrderRequestDto req) {
        Long currentUserId = SecurityUtils.currentUserId();

        WorkOrder w = workOrderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("work order not found"));

        Client client = clientRepository.findById(req.clientId)
                .orElseThrow(() -> new NotFoundException("client not found"));

        Client owner = null;
        if (req.ownerId != null) {
            owner = clientRepository.findById(req.ownerId)
                    .orElseThrow(() -> new NotFoundException("owner not found"));
        }

        Appliance appliance = null;
        if (req.applianceId != null) {
            appliance = applianceRepository.findById(req.applianceId)
                    .orElseThrow(() -> new NotFoundException("appliance not found"));
        }

        User assigned = null;
        if (req.assignedUserId != null) {
            assigned = userRepository.findById(req.assignedUserId)
                    .orElseThrow(() -> new NotFoundException("assigned user not found"));
        }

        User lastUpdatedUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new NotFoundException("last updated user not found"));

        WorkOrderMapper.updateEntity(w, req, client, owner, appliance, assigned, lastUpdatedUser);
        w = workOrderRepository.save(w);
        return WorkOrderMapper.toResponse(w);
    }

    @Transactional
    public void delete(Long id) {
        workOrderRepository.deleteById(id);
    }

}
