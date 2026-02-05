package ruben.springboot.service_management.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ruben.springboot.service_management.models.Appliance;

public interface ApplianceRepository extends JpaRepository<Appliance, Long>{

    List<Appliance> findByActiveTrueOrderByBrandAscModelAsc();

    List<Appliance> findByClientIdAndActiveTrueOrderByBrandAscModelAsc(Long clientId);

    List<Appliance> findByClientIdOrderByIdDesc(Long clientId);

}
