package ruben.springboot.service_management.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import ruben.springboot.service_management.models.dto.ApplianceRequestDto;
import ruben.springboot.service_management.models.dto.ApplianceResponseDto;
import ruben.springboot.service_management.services.ApplianceService;

@RestController
@RequestMapping("/api/appliances")
public class ApplianceController {

    @Autowired
    private ApplianceService service;

    public ApplianceController(ApplianceService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApplianceResponseDto create(@Valid @RequestBody ApplianceRequestDto req) {
        return service.create(req);
    }

    // /api/appliances?clientId=123  -> lista por cliente
    @GetMapping
    public List<ApplianceResponseDto> list(@RequestParam(required = false) Long clientId) {
        if (clientId != null) return service.listActiveByClient(clientId);
        return service.listActive();
    }

    @GetMapping("/{id}")
    public ApplianceResponseDto get(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public ApplianceResponseDto update(@PathVariable Long id, @Valid @RequestBody ApplianceRequestDto req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        service.setNotActive(id);
    }

}