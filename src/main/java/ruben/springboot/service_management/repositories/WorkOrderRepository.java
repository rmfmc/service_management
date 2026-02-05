package ruben.springboot.service_management.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ruben.springboot.service_management.models.WorkOrder;
import ruben.springboot.service_management.models.dto.WorkOrderListDto;

public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {

    List<WorkOrder> findAllByOrderByCreatedAtDesc();

    List<WorkOrder> findByClientIdOrderByCreatedAtDesc(Long clientId);

    List<WorkOrder> findByAssignedUserIdOrderByCreatedAtDesc(Long userId);

    List<WorkOrder> findByStatusOrderByCreatedAtDesc(String status);

    @Query("""
            select new ruben.springboot.service_management.models.dto.WorkOrderListDto(
              wo.id, wo.issueDescription,  wo.status, wo.priority, wo.createdAt,
              c.name, c.address, c.city, c.phone,
              a.applianceType, a.brand,
              u.username
            )
            from WorkOrder wo
            join wo.client c
            left join wo.appliance a
            left join wo.assignedUser u
            order by wo.createdAt desc
            """)
    List<WorkOrderListDto> findAllForList();

}
