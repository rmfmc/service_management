package ruben.springboot.service_management.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ruben.springboot.service_management.models.User;

public interface UserRepository extends JpaRepository<User, Long>{

    boolean existsByUsernameIgnoreCase(String username);

    Optional<User> findByUsernameIgnoreCase(String username);
    
}
