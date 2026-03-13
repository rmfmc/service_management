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
import ruben.springboot.service_management.models.dtos.responses.PageResponse;
import ruben.springboot.service_management.models.mappers.PageMapper;
import ruben.springboot.service_management.services.ApplianceService;

@RestController
@RequestMapping("/api/appliances")
public class ApplianceController {

    @Autowired
    private ApplianceService service;

    @PostMapping("/{addressId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ApplianceResponseDto create(@PathVariable Long addressId, @Valid @RequestBody ApplianceRequestDto req) {
        return service.createForAddress(addressId, req);
    }

    @PutMapping("/{addressId}/{id}")
    public ApplianceResponseDto update(@PathVariable Long addressId, @PathVariable Long id, @Valid @RequestBody ApplianceRequestDto req) {
        return service.updateByAddressAndId(addressId, id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApplianceResponseDto delete(@PathVariable Long id) {
        return service.setNotActiveById(id);
    }

    @GetMapping
    public PageResponse<ApplianceListDto> list(@RequestParam(defaultValue = "0") int page) {
        return PageMapper.toResponse(service.listAll(page));
    }

    @GetMapping("/address/{addressId}")
    public List<ApplianceListDto> list(@PathVariable Long addressId) {
        return service.listByAddress(addressId);
    }

    @GetMapping("/{id}")
    public ApplianceResponseDto get(@PathVariable Long id) {
        return service.getById(id);
    }


}