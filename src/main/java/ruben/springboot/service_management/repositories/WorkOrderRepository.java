package ruben.springboot.service_management.repositories;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import ruben.springboot.service_management.models.Client;
import ruben.springboot.service_management.models.WorkOrder;
import ruben.springboot.service_management.models.enums.WorkOrderStatus;

public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {

  List<WorkOrder> findAllByOrderByCreatedAtDesc();

  List<WorkOrder> findByClientIdOrderByCreatedAtDesc(Long clientId);

  List<WorkOrder> findByAssignedUserIdAndScheduledAt(Long userId, LocalDate date);

  List<WorkOrder> findByStatusOrderByCreatedAtDesc(String status);

  List<WorkOrder> findByClient(Client client);

  List<WorkOrder> findByClientId(Long clientId);

  List<WorkOrder> findByAddressId(Long addressId);

  List<WorkOrder> findByStatus(WorkOrderStatus status);

  List<WorkOrder> findByAssignedUserId(Long assignedUserId);

  Page<WorkOrder> findAll(Pageable pageable);

  Page<WorkOrder> findByScheduledAt(LocalDate date, Pageable pageable);

  Page<WorkOrder> findByStatusIn(Collection<WorkOrderStatus> statuses, Pageable pageable);

  Page<WorkOrder> findByAssignedUserIdAndScheduledAt(Long userId, LocalDate date, Pageable pageable);

  Page<WorkOrder> findByCreatedAt(LocalDate date, Pageable pageable);

  Optional<WorkOrder> findByIdAndAssignedUserId(Long id, Long assignedUserId);

  boolean existsByAssignedUserId(Long id);

}
