package ruben.springboot.service_management.controllers;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ruben.springboot.service_management.models.dtos.lists.WorkOrderListDto;
import ruben.springboot.service_management.models.dtos.requests.WorkOrderFullRequestDto;
import ruben.springboot.service_management.models.dtos.responses.WorkOrderResponseAdminDto;
import ruben.springboot.service_management.models.dtos.responses.WorkOrderResponseTechDto;
import ruben.springboot.service_management.services.WorkOrderService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/work-orders")
public class WorkOrderController {

    @Autowired
    private WorkOrderService service;

    @PostMapping("/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public WorkOrderResponseAdminDto create(@Valid @RequestBody WorkOrderFullRequestDto req) {
        return service.createFull(req);
    }

    @PutMapping("/admin/{id}")
    public WorkOrderResponseAdminDto update(@PathVariable Long id, @Valid @RequestBody WorkOrderFullRequestDto req) {
        return service.updateFull(id, req);
    }
    
    @DeleteMapping("/admin/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/admin/{id}")
    public WorkOrderResponseAdminDto adminGetById(@PathVariable Long id) {
        return service.adminGetById(id);
    }

    @GetMapping("/admin")
    public Page<WorkOrderListDto> adminListAll(@RequestParam(defaultValue = "0") int page) {
        return service.adminListAll(page);
    }

    @GetMapping("/admin/scheduled")
    public Page<WorkOrderListDto> adminListByScheduledDate(@RequestParam LocalDate date, @RequestParam(defaultValue = "0") int page) {
        return service.adminListByScheduledDate(date, page);
    }

    @GetMapping("/admin/pending")
    public Page<WorkOrderListDto> adminListPending(@RequestParam(defaultValue = "0") int page) {
        return service.adminListPending(page);
    }

    @GetMapping("/admin/user-scheduled")
    public Page<WorkOrderListDto> adminListByScheduledDateAndUserId(@RequestParam LocalDate date, @RequestParam(defaultValue = "0") int page) {
        return service.adminListByUserAndScheduledDate(date, page);
    }

    @GetMapping("/tech")
    public Page<WorkOrderListDto> techListByScheduledDateAndUserId(@RequestParam LocalDate date, @RequestParam(defaultValue = "0") int page) {
        return service.techListByUserAndScheduledDate(date, page);
    }

    @GetMapping("/tech/{id}")
    public WorkOrderResponseTechDto techGetById(@PathVariable Long id) {
        return service.techGetById(id);
    }


}
