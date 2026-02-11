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
import ruben.springboot.service_management.errors.AlreadyExistsException;
import ruben.springboot.service_management.errors.NotFoundException;
import ruben.springboot.service_management.models.Address;
import ruben.springboot.service_management.models.Appliance;
import ruben.springboot.service_management.models.Client;
import ruben.springboot.service_management.models.User;
import ruben.springboot.service_management.models.WorkOrder;
import ruben.springboot.service_management.models.WorkOrderCharge;
import ruben.springboot.service_management.models.dtos.lists.WorkOrderListDto;
import ruben.springboot.service_management.models.dtos.requests.ApplianceRequestDto;
import ruben.springboot.service_management.models.dtos.requests.ClientRequestDto;
import ruben.springboot.service_management.models.dtos.requests.WorkOrderChargeRequestDto;
import ruben.springboot.service_management.models.dtos.requests.WorkOrderFullRequestDto;
import ruben.springboot.service_management.models.dtos.requests.WorkOrderRequestDto;
import ruben.springboot.service_management.models.dtos.responses.WorkOrderResponseDto;
import ruben.springboot.service_management.models.enums.ChargeType;
import ruben.springboot.service_management.models.enums.WorkOrderPriority;
import ruben.springboot.service_management.models.enums.WorkOrderStatus;
import ruben.springboot.service_management.models.mappers.ApplianceMapper;
import ruben.springboot.service_management.models.mappers.ClientMapper;
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

    @Transactional
    public WorkOrderResponseDto createFull(WorkOrderFullRequestDto req, Long currentUserId) {

        if (req == null){
            throw new IllegalArgumentException("request is required");
        }

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new NotFoundException("Current user not found: " + currentUserId));

        // 1) CLIENT (id o create; si viene dto y hay id -> update)
        Client client = clientService.resolve(req.clientId, req.clientDto);

        // 2) ADDRESS (id o create; si viene dto y hay id -> update)
        Address address = addressService.resolve(req.addressId, req.addressDto, client);

        // 3) APPLIANCES (ids y/o crear nuevos)
        Set<Appliance> appliances = applianceService.resolve(req.appliances, req.appliances, address);

        // 4) WORK ORDER
        WorkOrder w = new WorkOrder();
        w.setClient(client);
        w.setAddress(address);
        w.getAppliances().clear();
        w.getAppliances().addAll(appliances);

        w.setAssignedUser(req.workOrderDto.assignedUserId == null ? null
                : userRepository.findById(req.workOrderDto.assignedUserId)
                        .orElseThrow(() -> new NotFoundException("AssignedUser not found: " + req.workOrderDto.assignedUserId)));

        w.setCreatedUser(currentUser);
        w.setLastUpdatedUser(currentUser);

        w.setIssueDescription(req.workOrderDto.issueDescription);
        w.setStatus(req.workOrderDto.status == null ? WorkOrderStatus.NEW : req.workOrderDto.status);
        w.setPriority(req.workOrderDto.priority == null ? WorkOrderPriority.MEDIUM : req.workOrderDto.priority);
        w.setNotes(req.workOrderDto.notes);
        w.setWorkPerformed(req.workOrderDto.workPerformed);

        w.setDiscountVisit(Boolean.TRUE.equals(req.workOrderDto.discountVisit));
        w.setBillTo(req.workOrderDto.billTo);

        w.setScheduledAt(req.workOrderDto.scheduledAt);

        w.setTenant(req.workOrderDto.tenantId == null ? null : clientService.resolve(req.workOrderDto.tenantId, null)); // solo id, sin update

        w.setCreatedAt(LocalDateTime.now());
        w.setLastUpdatedAt(LocalDateTime.now());

        // closedAt si ya entra cerrada
        if ((w.getStatus() == WorkOrderStatus.CLOSED || w.getStatus() == WorkOrderStatus.APPLIANCE_INSTALLED)
                && w.getClosedAt() == null) {
            w.setClosedAt(LocalDateTime.now());
        }

        // 5) CHARGES + totalPrice
        boolean visitChargedAndPaid = false;
        BigDecimal visitPrice = BigDecimal.ZERO;
        BigDecimal acumulativePrice = BigDecimal.ZERO;

        if (req.charges != null) {
            for (WorkOrderChargeRequestDto woc : req.charges) {

                WorkOrderCharge charge = WorkOrderChargeMapper.toEntity(woc, currentUserId);
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

        if (w.getDiscountVisit() && visitChargedAndPaid) {
            w.setTotalPrice(acumulativePrice.subtract(visitPrice));
        } else {
            w.setTotalPrice(acumulativePrice);
        }

        // 6) SAVE
        WorkOrder saved = workOrderRepository.save(w);

        // 7) RESPONSE
        return workOrderMapper.toResponse(saved);
    }

    // @Transactional(readOnly = true)
    // public List<WorkOrderListDto> list(Long clientId, Long assignedUserId, String status) {

    //     List<WorkOrderListDto> list;

    //     if (clientId != null)
    //         list = workOrderRepository.findByClientIdOrderByCreatedAtDesc(clientId).stream()
    //                 .map(w -> WorkOrderMapper.toList(w)).toList();
    //     else if (assignedUserId != null)
    //         list = workOrderRepository.findByAssignedUserIdOrderByCreatedAtDesc(assignedUserId).stream()
    //                 .map(w -> WorkOrderMapper.toList(w)).toList();
    //     else if (status != null)
    //         list = workOrderRepository.findByStatusOrderByCreatedAtDesc(status).stream()
    //                 .map(w -> WorkOrderMapper.toList(w)).toList();
    //     else
    //         // list = workOrderRepository.findAllByOrderByCreatedAtDesc();
    //         list = workOrderRepository.findAllForList();

    //     return list;
    // }

    // @Transactional(readOnly = true)
    // public WorkOrderResponseDto getById(Long id) {
    //     WorkOrder w = workOrderRepository.findById(id)
    //             .orElseThrow(() -> new NotFoundException("work order not found"));
    //     return WorkOrderMapper.toResponse(w);
    // }

    @Transactional
    public WorkOrderResponseDto update(Long id, WorkOrderRequestDto req) {

        if (id == null || id == 0 || !workOrderRepository.existsById(id)) {
            throw new NotFoundException("work order not found");
        }

        req.id = id;

        Client client = null;
        if (req.clientDto.id == null && !clientRepository.existsByPhone(req.clientDto.phone)) {
            // Client es nuevo y no el num de teléfono no existe: lo crea
            client = clientRepository.save(ClientMapper.toEntity(req.clientDto));
        } else if (req.clientDto.id == null && clientRepository.existsByPhone(req.clientDto.phone)) {
            // Client es nuevo y no el num de teléfono ya existe: da error y da nombre de
            // cliente con ese teléfono
            String alreadyUsedPhoneName = clientRepository.findByPhone(req.clientDto.phone).get().getName();
            throw new AlreadyExistsException("teléfono de cliente ya utilizado por cliente: " + alreadyUsedPhoneName);
        } else if (clientRepository.existsById(req.clientDto.id)) {
            // Client id existe
            Client clientDb = clientRepository.findById(req.clientDto.id).get();

            // Cliente es diferente al que viene de la BD y el numero de teléfono no existe:
            // actualiza y guarda
            if (!clientDb.equals(ClientMapper.toEntity(req.clientDto))
                    && !clientRepository.existsByPhone(req.clientDto.phone)) {
                patchClient(clientDb, req.clientDto);
                client = clientRepository.save(clientDb);
            } else if (!clientDb.equals(ClientMapper.toEntity(req.clientDto))
                    && clientRepository.existsByPhone(req.clientDto.phone)) {
                // Cliente es diferente al que viene de la BD y el numero de teléfono existe: da
                // error y da nombre de cliente con ese teléfono
                String alreadyUsedPhoneName = clientRepository.findByPhone(req.clientDto.phone).get().getName();
                throw new AlreadyExistsException(
                        "teléfono de cliente ya utilizado por cliente: " + alreadyUsedPhoneName);
            } else {
                // Cliente es igual que el de la BD: referencia al de la BD
                client = clientDb;
            }
        } else {
            throw new NotFoundException("client not found");
        }

        Client owner = null;
        if (req.ownerDto != null) {
            if (req.ownerDto.id == null && !clientRepository.existsByPhone(req.ownerDto.phone)) {
                owner = clientRepository.save(ClientMapper.toEntity(req.ownerDto));
            } else if (req.ownerDto.id == null && clientRepository.existsByPhone(req.ownerDto.phone)) {
                String alreadyUsedPhoneName = clientRepository.findByPhone(req.ownerDto.phone).get().getName();
                throw new AlreadyExistsException(
                        "teléfono de dueño ya utilizado por cliente1: " + alreadyUsedPhoneName);
            } else if (clientRepository.existsById(req.ownerDto.id)) {

                Client ownerDb = clientRepository.findById(req.ownerDto.id).get();

                if (!ownerDb.equals(ClientMapper.toEntity(req.ownerDto))
                        && !clientRepository.existsByPhone(req.ownerDto.phone)) {
                    patchClient(ownerDb, req.ownerDto);
                    owner = clientRepository.save(ownerDb);
                } else if (!ownerDb.equals(ClientMapper.toEntity(req.ownerDto))
                        && clientRepository.existsByPhone(req.ownerDto.phone)) {
                    String alreadyUsedPhoneName = clientRepository.findByPhone(req.ownerDto.phone).get().getName();
                    throw new AlreadyExistsException(
                            "teléfono de dueño ya utilizado por cliente2: " + alreadyUsedPhoneName);
                } else {
                    owner = ownerDb;
                }
            }
        }

        Appliance appliance = null;
        System.out.println(req.applianceDto.toString());
        if (req.applianceDto != null) {
            if (req.applianceDto.id == null) {
                appliance = applianceRepository.save(ApplianceMapper.toEntity(req.applianceDto, client));
            } else if (applianceRepository.existsById(req.applianceDto.id)) {

                Appliance applianceDb = applianceRepository.findById(req.applianceDto.id).get();

                if (!applianceDb.equals(ApplianceMapper.toEntity(req.applianceDto, client))) {
                    patchAppliance(applianceDb, req.applianceDto);
                    appliance = applianceRepository.save(applianceDb);
                } else {
                    appliance = applianceDb;
                }
            }
        }

        User assigned = null;
        if (req.assignedUserId != null) {
            assigned = userRepository.findById(req.assignedUserId)
                    .orElseThrow(() -> new NotFoundException("assigned user not found"));
        }

        Long currentUserId = SecurityUtils.currentUserId();
        User createdUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new NotFoundException("created user not found"));

        User lastUpdatedUser = createdUser;

        if (req.sheduledAt == null) {
            req.sheduledAt = LocalDate.now();
            req.sheduledAt.plusDays(1);
        }

        WorkOrder w = WorkOrderMapper.toEntity(req, client, owner, appliance, assigned, createdUser,
                lastUpdatedUser);
        w = workOrderRepository.save(w);
        return WorkOrderMapper.toResponse(w);
    }

    @Transactional
    public void delete(Long id) {
        workOrderRepository.deleteById(id);
    }

}
