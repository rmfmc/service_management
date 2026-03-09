package ruben.springboot.service_management.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ruben.springboot.service_management.models.dtos.lists.AddressListDto;
import ruben.springboot.service_management.models.dtos.requests.AddressRequestDto;
import ruben.springboot.service_management.models.dtos.responses.AddressResponseDto;
import ruben.springboot.service_management.services.AddressService;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressService service;

    @PostMapping("/{clientId}")
    @ResponseStatus(HttpStatus.CREATED)
    public AddressResponseDto create(@PathVariable Long clientId, @Valid @RequestBody AddressRequestDto req) {
        return service.create(clientId, req);
    }

    @PutMapping("/{id}/{clientId}")
    public AddressResponseDto update(@PathVariable Long id, @PathVariable Long clientId, @Valid @RequestBody AddressRequestDto req) {
        return service.update(clientId, id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping
    public Page<AddressListDto> list(@RequestParam(defaultValue = "0") int page) {
        return service.list(page);
    }

    @GetMapping("/{id}")
    public AddressResponseDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/client/{clientId}")
    public List<AddressListDto> listByClientId(@PathVariable Long clientId) {
        return service.listByClientId(clientId);
    }

}
