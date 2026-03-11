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
import ruben.springboot.service_management.models.dtos.BrandDto;
import ruben.springboot.service_management.services.BrandService;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

    @Autowired
    private BrandService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BrandDto create(@Valid @RequestBody BrandDto req) {
        return service.create(req);
    }

    @PutMapping("/{id}")
    public BrandDto update(@PathVariable Long id, @Valid @RequestBody BrandDto req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }

    @GetMapping
    public List<BrandDto> list() {
        return service.listAll();
    }

    @GetMapping("/{id}")
    public BrandDto getById(@PathVariable Long id) {
        return service.getById(id);
    }
}

