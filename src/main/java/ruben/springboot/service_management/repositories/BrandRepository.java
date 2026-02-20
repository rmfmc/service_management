package ruben.springboot.service_management.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ruben.springboot.service_management.models.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    Optional<Brand> findByNameIgnoreCase(String name);
    
    boolean existsByNameIgnoreCase(String name);
}
