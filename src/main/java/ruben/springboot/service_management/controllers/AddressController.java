package ruben.springboot.service_management.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ruben.springboot.service_management.models.dtos.lists.AddressListDto;
import ruben.springboot.service_management.services.AddressService;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressService service;

    @GetMapping
    public Page<AddressListDto> list(@RequestParam(defaultValue = "0") int pageInt) {
        return service.list(pageInt);
    }
    
    @GetMapping("/{clientId}")
    public List<AddressListDto> listByClientId(@PathVariable Long clientId) {
        return service.listByClientId(clientId);
    }

}
