package ruben.springboot.service_management.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ruben.springboot.service_management.models.ApplianceType;

public interface ApplianceTypeRepository extends JpaRepository<ApplianceType, Long> {

    Optional<ApplianceType> findByNameIgnoreCase(String name);
    
    boolean existsByNameIgnoreCase(String name);
}
