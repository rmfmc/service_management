package ruben.springboot.service_management.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ruben.springboot.service_management.errors.AlreadyExistsException;
import ruben.springboot.service_management.errors.NotFoundException;
import ruben.springboot.service_management.models.User;
import ruben.springboot.service_management.models.dtos.requests.UserRequestDto;
import ruben.springboot.service_management.models.dtos.requests.UserWithoutPasswordRequestDto;
import ruben.springboot.service_management.models.dtos.responses.UserResponseDto;
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
            throw new AlreadyExistsException("username already exists");
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

    @Transactional
    public UserResponseDto updateData(Long id, UserWithoutPasswordRequestDto req) {

        Optional<User> userOpt = repository.findByUsernameIgnoreCase(req.username);

        if (userOpt.isPresent() && !userOpt.get().getId().equals(id)) {
            throw new AlreadyExistsException("username already exists");
        }

        User userDB = repository.findById(id).orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        userDB.setName(req.name.trim());
        userDB.setPhone(req.phone.trim());
        userDB.setUsername(req.username.trim().toLowerCase());
        userDB.setRole(req.role);
        userDB.setActive(req.active);
        userDB = repository.save(userDB);

        return UserMapper.toResponse(userDB);
    }

    @Transactional
    public UserResponseDto updatePassword(Long id, String password) {

        User userDB = repository.findById(id).orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        userDB.setPasswordHash(encoder.encode(password));
        
        userDB = repository.save(userDB);

        return UserMapper.toResponse(userDB);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> list(){
        return repository.findAll().stream().map(u -> UserMapper.toResponse(u)).toList();
    }

}
