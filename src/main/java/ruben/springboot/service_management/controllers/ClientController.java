package ruben.springboot.service_management.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ruben.springboot.service_management.models.dto.ClientListDto;
import ruben.springboot.service_management.models.dto.ClientRequestDto;
import ruben.springboot.service_management.models.dto.ClientResponseDto;
import ruben.springboot.service_management.services.ClientService;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService service;

    @GetMapping
    public List<ClientListDto> list() {
        return service.list();
    }

    @PostMapping
    public ClientResponseDto create(@Valid @RequestBody ClientRequestDto request) {
        return service.create(request);
    }

}
