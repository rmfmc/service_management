package ruben.springboot.service_management.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import ruben.springboot.service_management.models.Address;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByClientId(Long clientId);

    Page<Address> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"client"})
    Optional<Address> findById(Long id);
}

