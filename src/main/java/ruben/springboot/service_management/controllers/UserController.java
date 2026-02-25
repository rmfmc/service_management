package ruben.springboot.service_management.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ruben.springboot.service_management.models.dtos.requests.UserPasswordRequestDto;
import ruben.springboot.service_management.models.dtos.requests.UserRequestDto;
import ruben.springboot.service_management.models.dtos.requests.UserWithoutPasswordRequestDto;
import ruben.springboot.service_management.models.dtos.responses.UserResponseDto;
import ruben.springboot.service_management.services.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto create(@Valid @RequestBody UserRequestDto req) {
        return service.create(req);
    }

    @PutMapping("/{id}")
    public UserResponseDto updateData(@PathVariable Long id, @Valid @RequestBody UserWithoutPasswordRequestDto req) {
        return service.updateData(id, req);
    }

    @PutMapping("/{id}/password")
    public UserResponseDto updatePassword(@PathVariable Long id, @Valid @RequestBody UserPasswordRequestDto req) {
        return service.updatePassword(id, req);
    }

    @GetMapping
    public List<UserResponseDto> list() {
        return service.list();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }
    

}
