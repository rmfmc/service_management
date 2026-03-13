package ruben.springboot.service_management.repositories;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
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

  @EntityGraph(attributePaths = {
      "client",
      "address",
      "assignedUser",
      "appliances",
      "appliances.applianceType",
      "appliances.brand"
  })
  Page<WorkOrder> findAll(Pageable pageable);

  @EntityGraph(attributePaths = {
      "client",
      "address",
      "assignedUser",
      "appliances",
      "appliances.applianceType",
      "appliances.brand"
  })
  Page<WorkOrder> findByScheduledAt(LocalDate date, Pageable pageable);

  @EntityGraph(attributePaths = {
      "client",
      "address",
      "assignedUser",
      "appliances",
      "appliances.applianceType",
      "appliances.brand"
  })
  Page<WorkOrder> findByStatusIn(Collection<WorkOrderStatus> statuses, Pageable pageable);

   @EntityGraph(attributePaths = {
      "client",
      "address",
      "assignedUser",
      "appliances",
      "appliances.applianceType",
      "appliances.brand"
  })
  Page<WorkOrder> findByAssignedUserIdAndScheduledAt(Long userId, LocalDate date, Pageable pageable);

  @EntityGraph(attributePaths = {
      "client",
      "address",
      "assignedUser",
      "appliances",
      "appliances.applianceType",
      "appliances.brand"
  })
  Page<WorkOrder> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

   @EntityGraph(attributePaths = {
      "client",
      "address",
      "tenant",
      "assignedUser",
      "createdUser",
      "lastUpdatedUser",
      "appliances",
      "appliances.applianceType",
      "appliances.brand",
      "charges"
  })
  Optional<WorkOrder> findByIdAndAssignedUserId(Long id, Long assignedUserId);

  @EntityGraph(attributePaths = {
      "client",
      "address",
      "tenant",
      "assignedUser",
      "createdUser",
      "lastUpdatedUser",
      "appliances",
      "appliances.applianceType",
      "appliances.brand",
      "charges"
  })
  Optional<WorkOrder> findById(Long id);

  boolean existsByAssignedUserId(Long id);

}
