package ruben.springboot.service_management.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import ruben.springboot.service_management.models.mappers.ApplianceMapper;
import ruben.springboot.service_management.errors.NotFoundException;
import ruben.springboot.service_management.models.Address;
import ruben.springboot.service_management.models.Appliance;
import ruben.springboot.service_management.models.dtos.lists.ApplianceListDto;
import ruben.springboot.service_management.models.dtos.requests.ApplianceRequestDto;
import ruben.springboot.service_management.models.dtos.responses.ApplianceResponseDto;
import ruben.springboot.service_management.repositories.AddressRepository;
import ruben.springboot.service_management.repositories.ApplianceRepository;

@Service
public class ApplianceService {

    @Autowired
    private ApplianceRepository applianceRepository;

    @Autowired
    private ApplianceMapper applianceMapper;

    @Autowired
    private AddressRepository addressRepository;

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
                    throw new IllegalArgumentException(
                            "Appliance " + a.getId() + " does not belong to address " + address.getId());
                }
                result.add(a);
            }
        }

        // crear nuevos
        if (applianceDtos != null) {
            for (ApplianceRequestDto dto : applianceDtos) {

                Appliance appliance = new Appliance();
                appliance.setAddress(address);
                applianceMapper.update(appliance, dto);
                
                result.add(applianceRepository.save(appliance));
            }
        }

        return result;
    }

    @Transactional
    public ApplianceResponseDto createForAddress(Long addressId, ApplianceRequestDto req) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new NotFoundException("Address not found: " + addressId));

        Appliance appliance = applianceMapper.toEntity(req, addressId);
        appliance.setAddress(address);

        return applianceMapper.toResponse(applianceRepository.save(appliance));
    }
    
    @Transactional
    public ApplianceResponseDto updateByAddressAndId(Long addressId, Long applianceId, ApplianceRequestDto req) {
        Appliance appliance = findByAddressAndIdOrThrow(addressId, applianceId);
        applianceMapper.update(appliance, req);
        return applianceMapper.toResponse(applianceRepository.save(appliance));
    }

    @Transactional
    public void setNotActiveById(Long id) {
        Appliance appliance = applianceRepository.findById(id).orElseThrow(() -> new NotFoundException("Appliance not found: " + id));
        appliance.setActive(false);
        applianceRepository.save(appliance);
    }

    @Transactional(readOnly = true)
    public List<ApplianceListDto> listByAddress(Long addressId) {
        ensureAddressExists(addressId);
        return applianceRepository.findByAddressIdOrderByIdAsc(addressId).stream().map(ApplianceMapper::toList)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ApplianceListDto> listAll() {
        return applianceRepository.findAll().stream().map(ApplianceMapper::toList).toList();
    }

    @Transactional(readOnly = true)
    public ApplianceResponseDto getById(Long id) {
        Optional<Appliance> applianceOpt = applianceRepository.findById(id);
        return applianceMapper.toResponse(applianceOpt.orElseThrow(() -> new NotFoundException("Appliance not found: " + id)));
    }


    private void ensureAddressExists(Long addressId) {
        if (!addressRepository.existsById(addressId)) {
            throw new NotFoundException("Address not found: " + addressId);
        }
    }

    private Appliance findByAddressAndIdOrThrow(Long addressId, Long applianceId) {

        ensureAddressExists(addressId);

        Appliance appliance = applianceRepository.findById(applianceId)
                .orElseThrow(() -> new NotFoundException("Appliance not found: " + applianceId));

        if (!appliance.getAddress().getId().equals(addressId)) {
            throw new IllegalArgumentException(
                    "Appliance " + applianceId + " does not belong to address " + addressId);
        }

        return appliance;
    }

}
