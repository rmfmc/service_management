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
import ruben.springboot.service_management.models.dtos.lists.ApplianceListDto;
import ruben.springboot.service_management.models.dtos.requests.ApplianceRequestDto;
import ruben.springboot.service_management.models.dtos.responses.ApplianceResponseDto;
import ruben.springboot.service_management.services.ApplianceService;

@RestController
@RequestMapping("/api/appliances")
public class ApplianceController {

    @Autowired
    private ApplianceService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApplianceResponseDto create(@PathVariable Long addressId, @Valid @RequestBody ApplianceRequestDto req) {
        return service.createForAddress(addressId, req);
    }

    @GetMapping
    public List<ApplianceListDto> list(@PathVariable Long addressId) {
        return service.listByAddress(addressId);
    }

    @GetMapping("/{id}")
    public ApplianceResponseDto get(@PathVariable Long addressId, @PathVariable Long id) {
        return service.getByAddressAndId(addressId, id);
    }

    @PutMapping("/{id}")
    public ApplianceResponseDto update(
            @PathVariable Long addressId,
            @PathVariable Long id,
            @Valid @RequestBody ApplianceRequestDto req) {
        return service.updateByAddressAndId(addressId, id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long addressId, @PathVariable Long id) {
        service.setNotActiveByAddressAndId(addressId, id);
    }

}