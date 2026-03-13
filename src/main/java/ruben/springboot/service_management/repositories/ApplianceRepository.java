package ruben.springboot.service_management.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ruben.springboot.service_management.models.Appliance;

public interface ApplianceRepository extends JpaRepository<Appliance, Long>{

    List<Appliance> findByActiveTrueOrderByBrandAscModelAsc();

    List<Appliance> findByAddressId(Long addressId);

    List<Appliance> findByApplianceTypeId(Long applianceTypeId);
    
    List<Appliance> findByBrandId(Long brandId);

    List<Appliance> findByAddressIdOrderByIdAsc(Long addressId);

    Optional<Appliance> findById(Long id);

    Page<Appliance> findAll(Pageable page);

    @Query("""
            select a from Appliance a
            join fetch a.applianceType at
            left join fetch a.brand b
            where a.address.id in :addressIds
            order by a.id asc
            """)
    List<Appliance> findByAddressIdInWithTypeAndBrand(@Param("addressIds") Collection<Long> addressIds);
}
