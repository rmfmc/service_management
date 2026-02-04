package ruben.springboot.service_management.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ruben.springboot.service_management.errors.UsernameAlreadyExistsException;
import ruben.springboot.service_management.models.User;
import ruben.springboot.service_management.models.dto.UserRequestDto;
import ruben.springboot.service_management.models.dto.UserResponseDto;
import ruben.springboot.service_management.models.mappers.UserMapper;
import ruben.springboot.service_management.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Transactional
    public UserResponseDto create(UserRequestDto req) {

        if (repository.existsByUsernameIgnoreCase(req.username)) {
            throw new UsernameAlreadyExistsException();
        }

        User u = new User();
        u.setName(req.name.trim());
        u.setPhone(req.phone.trim());
        u.setUsername(req.username.trim().toLowerCase());
        u.setRole(req.role);
        u.setActive(true);
        u.setPasswordHash(encoder.encode(req.password));
        u = repository.save(u);

        return UserMapper.toResponse(u);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> list(){
        return repository.findAll().stream().map(u -> UserMapper.toResponse(u)).toList();
    }

}
