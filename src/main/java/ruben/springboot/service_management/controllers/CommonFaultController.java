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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ruben.springboot.service_management.models.dtos.CommonFaultDto;
import ruben.springboot.service_management.services.CommonFaultService;

@RestController
@RequestMapping("/api/common-faults")
public class CommonFaultController {

    @Autowired
    private CommonFaultService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommonFaultDto create(@Valid @RequestBody CommonFaultDto req) {
        return service.create(req);
    }

    @PutMapping("/{id}")
    public CommonFaultDto update(@PathVariable Long id, @Valid @RequestBody CommonFaultDto req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }

    @GetMapping
    public List<CommonFaultDto> list() {
        return service.listAll();
    }

    @GetMapping("/{id}")
    public CommonFaultDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/appliance-type/{applianceTypeId}")
    public List<CommonFaultDto> listByApplianceTypeId(@PathVariable Long applianceTypeId) {
        return service.listByApplianceTypeId(applianceTypeId);
    }
}
