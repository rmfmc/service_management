package ruben.springboot.service_management.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ruben.springboot.service_management.models.dtos.lists.ApplianceListDto;
import ruben.springboot.service_management.models.dtos.lists.ClientListDto;
import ruben.springboot.service_management.models.dtos.lists.WorkOrderListDto;
import ruben.springboot.service_management.models.dtos.requests.ClientRequestDto;
import ruben.springboot.service_management.models.dtos.responses.ClientResponseDto;
import ruben.springboot.service_management.services.ClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService service;

    @GetMapping
    public List<ClientListDto> list() {
        return service.list();
    }

    @PostMapping
    public ClientResponseDto create(@Valid @RequestBody ClientRequestDto request) {
        return service.create(request);
    }

    @GetMapping("/search")
    public List<ClientListDto> search(@RequestParam(name = "q", required = false) String q) {
        return service.search(q);
    }

    // @GetMapping("/{id}/work_orders")
    // public List<WorkOrderListDto> workOrders(@PathVariable Long id) {
    //     return service.findWorkOrdersByClientId(id);
    // }

    // @GetMapping("/{id}/appliances")
    // public List<ApplianceListDto> appliances(@PathVariable Long id) {
    //     return composeService.listAppliancesByClient(id);
    // }

}
