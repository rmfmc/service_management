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
import ruben.springboot.service_management.models.dtos.lists.ClientListDto;
import ruben.springboot.service_management.models.dtos.requests.ClientRequestDto;
import ruben.springboot.service_management.models.dtos.responses.ClientResponseDto;
import ruben.springboot.service_management.models.dtos.responses.PageResponse;
import ruben.springboot.service_management.models.mappers.PageMapper;
import ruben.springboot.service_management.services.ClientService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService service;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientResponseDto create(@Valid @RequestBody ClientRequestDto req) {
        return service.create(req);
    }
    
    @PutMapping("/{id}")
    public ClientResponseDto update(@PathVariable Long id, @Valid @RequestBody ClientRequestDto req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }

    @GetMapping
    public PageResponse<ClientListDto> list(@RequestParam(defaultValue = "0") int page) {
        return PageMapper.toResponse(service.listAll(page));
    }

    @GetMapping("/{id}")
    public ClientResponseDto getById(@PathVariable Long id) {
        return service.getById(id);
    }
    
    @GetMapping("/search")
    public List<ClientListDto> search(@RequestParam(required = false) String q) {
        return service.search(q);
    }

}
