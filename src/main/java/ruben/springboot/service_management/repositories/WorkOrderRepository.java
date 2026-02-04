package ruben.springboot.service_management.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ruben.springboot.service_management.models.WorkOrder;

public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {

    List<WorkOrder> findAllByOrderByCreatedAtDesc();

    List<WorkOrder> findByClientIdOrderByCreatedAtDesc(Long clientId);

    List<WorkOrder> findByAssignedUserIdOrderByCreatedAtDesc(Long userId);

    List<WorkOrder> findByStatusOrderByCreatedAtDesc(String status);
}
