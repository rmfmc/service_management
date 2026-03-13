package ruben.springboot.service_management.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ruben.springboot.service_management.errors.AlreadyExistsException;
import ruben.springboot.service_management.errors.ForbiddenException;
import ruben.springboot.service_management.errors.NotFoundException;
import ruben.springboot.service_management.models.User;
import ruben.springboot.service_management.models.dtos.requests.UserPasswordRequestDto;
import ruben.springboot.service_management.models.dtos.requests.UserRequestDto;
import ruben.springboot.service_management.models.dtos.requests.UserWithoutPasswordRequestDto;
import ruben.springboot.service_management.models.dtos.responses.UserResponseDto;
import ruben.springboot.service_management.models.mappers.UserMapper;
import ruben.springboot.service_management.repositories.UserRepository;
import ruben.springboot.service_management.repositories.WorkOrderRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Transactional
    public UserResponseDto create(UserRequestDto req) {
        if (repository.existsByUsernameIgnoreCase(req.username)) {
            throw new AlreadyExistsException("Usuario", req.username);
        }
        
        User u = UserMapper.toCreateEntityExceptPassword(req);
        u.setPasswordHash(encoder.encode(req.password));

        return UserMapper.toResponse(repository.save(u));
    }

    @Transactional
    public UserResponseDto updateData(Long id, UserWithoutPasswordRequestDto req) {

        Optional<User> userOpt = repository.findByUsernameIgnoreCase(req.username);

        if (userOpt.isPresent() && !userOpt.get().getId().equals(id)) {
            throw new AlreadyExistsException("Usuario", req.username);
        }

        User userDB = repository.findById(id).orElseThrow(() -> new NotFoundException("Usuario", id));
        UserMapper.updateEntityExceptPassword(userDB, req);
        
        return UserMapper.toResponse(repository.save(userDB));
    }

    @Transactional
    public UserResponseDto updatePassword(Long id, UserPasswordRequestDto req) {

        User userDB = repository.findById(id).orElseThrow(() -> new NotFoundException("Usuario", id));

        userDB.setPasswordHash(encoder.encode(req.password));
        
        return UserMapper.toResponse(repository.save(userDB));
    }

    @Transactional()
    public void delete(Long id){
        User user = repository.findById(id).orElseThrow(() -> new NotFoundException("Usuario", id));

        if (workOrderRepository.existsByAssignedUserId(id)) {
            throw new ForbiddenException("El usuario tiene avisos asignados. Desactiva el usuario o elimina los avisos relacionados");
        }
        repository.delete(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getById(Long id){
        return UserMapper.toResponse(repository.findById(id).orElseThrow(() -> new NotFoundException("Usuario", id)));
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> list(){
        return repository.findAll().stream().map(u -> UserMapper.toResponse(u)).toList();
    }

}
