package ruben.springboot.service_management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ruben.springboot.service_management.models.CommonFault;

import java.util.List;

public interface CommonFaultRepository extends JpaRepository<CommonFault, Long> {
    
    List<CommonFault> findByApplianceTypeId(Long applianceTypeId);

}
