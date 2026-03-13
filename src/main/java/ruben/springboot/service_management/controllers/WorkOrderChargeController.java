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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ruben.springboot.service_management.models.dtos.lists.WorkOrderChargeListDto;
import ruben.springboot.service_management.models.dtos.requests.WorkOrderChargeRequestDto;
import ruben.springboot.service_management.models.dtos.responses.PageResponse;
import ruben.springboot.service_management.models.dtos.responses.WorkOrderChargeResponseDto;
import ruben.springboot.service_management.models.mappers.PageMapper;
import ruben.springboot.service_management.services.WorkOrderChargeService;

@RestController
@RequestMapping("/api/charges")
public class WorkOrderChargeController {

    @Autowired
    private WorkOrderChargeService service;

    @PostMapping("/work-order/{workOrderId}")
    @ResponseStatus(HttpStatus.CREATED)
    public WorkOrderChargeResponseDto create(@PathVariable Long workOrderId, @Valid @RequestBody WorkOrderChargeRequestDto req) {
        return service.create(workOrderId, req);
    }

    @PutMapping("/{id}")
    public WorkOrderChargeResponseDto update(@PathVariable Long id, @Valid @RequestBody WorkOrderChargeRequestDto req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping
    public PageResponse<WorkOrderChargeListDto> listAll(@RequestParam(defaultValue = "0") int page) {
        return PageMapper.toResponse(service.listAll(page));
    }

    @GetMapping("/{id}")
    public WorkOrderChargeResponseDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/work-order/{workOrderId}")
    public List<WorkOrderChargeListDto> listByWorkOrderId(@PathVariable Long workOrderId) {
        return service.listByWorkOrderId(workOrderId);
    }
}
