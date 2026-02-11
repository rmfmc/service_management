package ruben.springboot.service_management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ruben.springboot.service_management.models.Address;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByClientId(Long clientId);
}

