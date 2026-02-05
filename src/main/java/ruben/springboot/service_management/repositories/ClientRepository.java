package ruben.springboot.service_management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ruben.springboot.service_management.models.Client;

public interface ClientRepository extends JpaRepository<Client, Long>{

    public boolean existsByPhone(String phone);
}
