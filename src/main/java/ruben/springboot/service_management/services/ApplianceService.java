package ruben.springboot.service_management.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import ruben.springboot.service_management.models.mappers.ApplianceMapper;
import ruben.springboot.service_management.errors.NotFoundException;
import ruben.springboot.service_management.models.Address;
import ruben.springboot.service_management.models.Appliance;
import ruben.springboot.service_management.models.Client;
import ruben.springboot.service_management.models.dtos.requests.ApplianceRequestDto;
import ruben.springboot.service_management.models.dtos.responses.ApplianceResponseDto;
import ruben.springboot.service_management.repositories.ApplianceRepository;
import ruben.springboot.service_management.repositories.ApplianceTypeRepository;
import ruben.springboot.service_management.repositories.BrandRepository;
import ruben.springboot.service_management.repositories.ClientRepository;

@Service
public class ApplianceService {

    @Autowired
    private ApplianceRepository applianceRepository;
    @Autowired
    private ApplianceMapper applianceMapper;
    @Autowired
    private ApplianceTypeRepository applianceTypeRepository;
    @Autowired
    private BrandRepository brandRepository;
    
    @Autowired
    private ClientRepository clientRepository;

    @Transactional
    public Set<Appliance> resolve(Set<Long> applianceIds, List<ApplianceRequestDto> applianceDtos, Address address) {

        Set<Appliance> result = new HashSet<>();

        // ids existentes
        if (applianceIds != null && !applianceIds.isEmpty()) {
            List<Appliance> found = applianceRepository.findAllById(applianceIds);
            if (found.size() != applianceIds.size()) {
                throw new NotFoundException("Some applianceIds not found");
            }
            for (Appliance a : found) {
                if (!a.getAddress().getId().equals(address.getId())) {
                    throw new IllegalArgumentException("Appliance " + a.getId() + " does not belong to address " + address.getId());
                }
                result.add(a);
            }
        }

        // crear nuevos
        if (applianceDtos != null) {
            for (ApplianceRequestDto dto : applianceDtos) {
                Appliance a = new Appliance();

                a.setAddress(address);

                a.setApplianceType(applianceTypeRepository.findById(dto.applianceTypeId)
                        .orElseThrow(() -> new NotFoundException("ApplianceType not found: " + dto.applianceTypeId)));

                if (dto.brandId != null) {
                    a.setBrand(brandRepository.findById(dto.brandId)
                            .orElseThrow(() -> new NotFoundException("Brand not found: " + dto.brandId)));
                } else {
                    a.setBrand(null);
                }

                a.setModel(dto.model);
                a.setSerialNumber(dto.serialNumber);
                a.setActive(dto.active != null ? dto.active : true);

                result.add(applianceRepository.save(a));
            }
        }

        return result;
    }

    // @Transactional
    // public ApplianceResponseDto create(ApplianceRequestDto req) {
    //     Client client = clientRepository.findById(req.clientId)
    //             .orElseThrow(() -> new NotFoundException("client not found"));

    //     Appliance a = applianceMapper.toEntity(req, client);
    //     a = applianceRepository.save(a);
    //     return applianceMapper.toResponse(a);
    // }

    // @Transactional(readOnly = true)
    // public List<ApplianceResponseDto> listActive() {
    //     return applianceRepository.findByActiveTrueOrderByBrandAscModelAsc()
    //             .stream().map(applianceMapper::toResponse).toList();
    // }

    // @Transactional(readOnly = true)
    // public List<ApplianceResponseDto> listActiveByClient(Long clientId) {
    //     return applianceRepository.findByClientIdAndActiveTrueOrderByBrandAscModelAsc(clientId)
    //             .stream().map(applianceMapper::toResponse).toList();
    // }

    // @Transactional(readOnly = true)
    // public ApplianceResponseDto getById(Long id) {
    //     Appliance a = applianceRepository.findById(id)
    //             .orElseThrow(() -> new NotFoundException("appliance not found"));
    //     return applianceMapper.toResponse(a);
    // }

    // @Transactional
    // public ApplianceResponseDto update(Long id, ApplianceRequestDto req) {
    //     Appliance a = applianceRepository.findById(id)
    //             .orElseThrow(() -> new NotFoundException("appliance not found"));

    //     Client client = clientRepository.findById(req.clientId)
    //             .orElseThrow(() -> new NotFoundException("client not found"));

    //     applianceMapper.updateEntity(a, req, client);
    //     a = applianceRepository.save(a);
    //     return applianceMapper.toResponse(a);
    // }

    // @Transactional
    // public void setNotActive(Long id) {
    //     Appliance a = applianceRepository.findById(id)
    //             .orElseThrow(() -> new NotFoundException("appliance not found"));

    //     a.setActive(false);
    //     applianceRepository.save(a);
    // }

    // @Transactional
    // public void addApplianceType(String name){
    //     ApplianceType(name);
    // }

}
