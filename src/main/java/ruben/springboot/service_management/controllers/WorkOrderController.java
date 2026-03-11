package ruben.springboot.service_management.controllers;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ruben.springboot.service_management.models.dtos.lists.WorkOrderListDto;
import ruben.springboot.service_management.models.dtos.requests.WorkOrderFullRequestDto;
import ruben.springboot.service_management.models.dtos.requests.WorkOrderTechUpdateRequestDto;
import ruben.springboot.service_management.models.dtos.responses.PageResponseDto;
import ruben.springboot.service_management.models.dtos.responses.WorkOrderResponseDto;
import ruben.springboot.service_management.models.mappers.PageMapper;
import ruben.springboot.service_management.services.WorkOrderService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/work-orders")
public class WorkOrderController {

    @Autowired
    private WorkOrderService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkOrderResponseDto create(@Valid @RequestBody WorkOrderFullRequestDto req) {
        return service.createFull(req);
    }

    @PutMapping("/{id}")
    public WorkOrderResponseDto update(@PathVariable Long id, @Valid @RequestBody WorkOrderFullRequestDto req) {
        return service.updateFull(id, req);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/{id}")
    public WorkOrderResponseDto adminGetById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public PageResponseDto<WorkOrderListDto> adminListAll(@RequestParam(defaultValue = "0") int page) {
        return PageMapper.toResponse(service.adminListAll(page));
    }

    @GetMapping("/scheduled")
    public PageResponseDto<WorkOrderListDto> adminListByScheduledDate(@RequestParam LocalDate date, @RequestParam(defaultValue = "0") int page) {
        return PageMapper.toResponse(service.adminListByScheduledDate(date, page));
    }

    @GetMapping("/pending")
    public PageResponseDto<WorkOrderListDto> adminListPending(@RequestParam(defaultValue = "0") int page) {
        return PageMapper.toResponse(service.adminListPending(page));
    }

    @GetMapping("/user-scheduled")
    public PageResponseDto<WorkOrderListDto> adminListByScheduledDateAndUserId(@RequestParam LocalDate date, @RequestParam(defaultValue = "0") int page) {
        return PageMapper.toResponse(service.adminListByUserAndScheduledDate(date, page));
    }

    @GetMapping("/created")
    public PageResponseDto<WorkOrderListDto> adminListByCreationDate(@RequestParam LocalDate date, @RequestParam(defaultValue = "0") int page) {
        return PageMapper.toResponse(service.adminListByCreationDate(date, page));
    }

    @PutMapping("/tech/{id}")
    public WorkOrderResponseDto techUpdate(@PathVariable Long id, @Valid @RequestBody WorkOrderTechUpdateRequestDto req) {
        return service.techUpdate(id,req);
    }

    @GetMapping("/tech")
    public PageResponseDto<WorkOrderListDto> techListByScheduledDateAndUserId(@RequestParam LocalDate date, @RequestParam(defaultValue = "0") int page) {
        return PageMapper.toResponse(service.techListByUserAndScheduledDate(date, page));
    }

    @GetMapping("/tech/{id}")
    public WorkOrderResponseDto techGetById(@PathVariable Long id) {
        return service.techGetById(id);
    }


}
