package ruben.springboot.service_management.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import ruben.springboot.service_management.models.enums.ApplianceType;
import ruben.springboot.service_management.models.mappers.ApplianceMapper;
import ruben.springboot.service_management.errors.NotFoundException;
import ruben.springboot.service_management.models.Appliance;
import ruben.springboot.service_management.models.Client;
import ruben.springboot.service_management.models.dtos.requests.ApplianceRequestDto;
import ruben.springboot.service_management.models.dtos.responses.ApplianceResponseDto;
import ruben.springboot.service_management.repositories.ApplianceRepository;
import ruben.springboot.service_management.repositories.ClientRepository;

@Service
public class ApplianceService {

    @Autowired
    private ApplianceRepository applianceRepository;
    
    @Autowired
    private ClientRepository clientRepository;

    @Transactional
    public ApplianceResponseDto create(ApplianceRequestDto req) {
        Client client = clientRepository.findById(req.clientId)
                .orElseThrow(() -> new NotFoundException("client not found"));

        Appliance a = ApplianceMapper.toEntity(req, client);
        a = applianceRepository.save(a);
        return ApplianceMapper.toResponse(a);
    }

    @Transactional(readOnly = true)
    public List<ApplianceResponseDto> listActive() {
        return applianceRepository.findByActiveTrueOrderByBrandAscModelAsc()
                .stream().map(ApplianceMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<ApplianceResponseDto> listActiveByClient(Long clientId) {
        return applianceRepository.findByClientIdAndActiveTrueOrderByBrandAscModelAsc(clientId)
                .stream().map(ApplianceMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ApplianceResponseDto getById(Long id) {
        Appliance a = applianceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("appliance not found"));
        return ApplianceMapper.toResponse(a);
    }

    @Transactional
    public ApplianceResponseDto update(Long id, ApplianceRequestDto req) {
        Appliance a = applianceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("appliance not found"));

        Client client = clientRepository.findById(req.clientId)
                .orElseThrow(() -> new NotFoundException("client not found"));

        ApplianceMapper.updateEntity(a, req, client);
        a = applianceRepository.save(a);
        return ApplianceMapper.toResponse(a);
    }

    @Transactional
    public void setNotActive(Long id) {
        Appliance a = applianceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("appliance not found"));

        a.setActive(false);
        applianceRepository.save(a);
    }

    // @Transactional
    // public void addApplianceType(String name){
    //     ApplianceType(name);
    // }

}
