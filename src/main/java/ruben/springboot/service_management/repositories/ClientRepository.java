package ruben.springboot.service_management.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ruben.springboot.service_management.models.Client;

public interface ClientRepository extends JpaRepository<Client, Long>{

    boolean existsByPhone(String phone);

    Optional<Client> findByPhone(String phone);
    
    boolean existsById(Long id);

    @Query("""
        select c from Client c
        where
            (:q is null)
            or (lower(c.name) like lower(concat('%', :q, '%')))
            or (lower(c.phone) like lower(concat('%', :q, '%')))
            or (c.phone2 is not null and lower(c.phone2) like lower(concat('%', :q, '%')))
        order by c.name asc
    """)
    List<Client> search(@Param("q") String q);

    boolean existsByPhoneAndIdNot(String newPhone, Long clientId);


}
