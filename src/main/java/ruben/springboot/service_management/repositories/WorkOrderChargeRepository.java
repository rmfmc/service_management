package ruben.springboot.service_management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ruben.springboot.service_management.models.WorkOrderCharge;

import java.util.List;

public interface WorkOrderChargeRepository extends JpaRepository<WorkOrderCharge, Long> {

    List<WorkOrderCharge> findByWorkOrderIdOrderByCreatedAtAsc(Long workOrderId);

    List<WorkOrderCharge> findByPaid(Boolean paid);
}
