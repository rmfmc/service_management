package ruben.springboot.service_management.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import ruben.springboot.service_management.models.WorkOrderCharge;

import java.util.List;
import java.util.Optional;

public interface WorkOrderChargeRepository extends JpaRepository<WorkOrderCharge, Long> {

    @Override
    @EntityGraph(attributePaths = {
            "workOrder",
            "workOrder.client",
            "workOrder.address",
            "workOrder.assignedUser",
            "workOrder.appliances",
            "workOrder.appliances.applianceType",
            "workOrder.appliances.brand"
    })
    Optional<WorkOrderCharge> findById(Long id);

     @EntityGraph(attributePaths = {
            "workOrder",
            "workOrder.client",
            "workOrder.address",
            "workOrder.assignedUser",
            "workOrder.appliances",
            "workOrder.appliances.applianceType",
            "workOrder.appliances.brand"
    })
    List<WorkOrderCharge> findAllByWorkOrderIdOrderByCreatedAtAsc(Long workOrderId);
    
     @EntityGraph(attributePaths = {
            "workOrder",
            "workOrder.client",
            "workOrder.address",
            "workOrder.assignedUser",
            "workOrder.appliances",
            "workOrder.appliances.applianceType",
            "workOrder.appliances.brand"
    })
    List<WorkOrderCharge> findAllByPaid(Boolean paid);
}
