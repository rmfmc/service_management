package ruben.springboot.service_management.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ruben.springboot.service_management.models.dtos.lists.WorkOrderListDto;
import ruben.springboot.service_management.models.dtos.requests.WorkOrderFullRequestDto;
import ruben.springboot.service_management.models.dtos.responses.WorkOrderResponseDto;
import ruben.springboot.service_management.services.WorkOrderService;

import java.util.List;

@RestController
@RequestMapping("/api/work-orders")
public class WorkOrderController {

    private final WorkOrderService service;

    public WorkOrderController(WorkOrderService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkOrderResponseDto create(@Valid @RequestBody WorkOrderFullRequestDto req) {
        return service.createFull(req);
    }

    // filtros opcionales:
    // /api/work-orders?clientId=1
    // /api/work-orders?assignedUserId=2
    // /api/work-orders?status=OPEN
    @GetMapping
    public List<WorkOrderListDto> list(@RequestParam(required = false) Long clientId,
                                          @RequestParam(required = false) Long assignedUserId,
                                          @RequestParam(required = false) String status) {
        return service.list(clientId, assignedUserId, status);
    }

    @GetMapping("/{id}")
    public WorkOrderResponseDto get(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public WorkOrderResponseDto update(@PathVariable Long id, @Valid @RequestBody WorkOrderFullRequestDto req) {
        return service.updateFull(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
