package ruben.springboot.service_management.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import ruben.springboot.service_management.models.Appliance;

public interface ApplianceRepository extends JpaRepository<Appliance, Long>{

    List<Appliance> findByActiveTrueOrderByBrandAscModelAsc();

    List<Appliance> findByAddressId(Long addressId);

    List<Appliance> findByApplianceTypeId(Long applianceTypeId);
    
    List<Appliance> findByBrandId(Long brandId);

    List<Appliance> findByAddressIdOrderByIdAsc(Long addressId);

    Optional<Appliance> findById(Long id);

    Page<Appliance> findAll(Pageable page);
}
