package ruben.springboot.service_management.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import ruben.springboot.service_management.models.mappers.ApplianceMapper;
import ruben.springboot.service_management.errors.BadRequestException;
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

    private int MAX_PAGE_SIZE = 30;

    @Transactional
    public Set<Appliance> resolve(Set<Long> applianceIds, List<ApplianceRequestDto> applianceDtos, Address address) {

        Set<Appliance> result = new HashSet<>();

        if (applianceIds != null && !applianceIds.isEmpty()) {
            List<Appliance> found = applianceRepository.findAllById(applianceIds);
            if (found.size() != applianceIds.size()) {
                throw new NotFoundException("Algunos electrodomésticos no existen");
            }
            for (Appliance a : found) {
                if (!a.getAddress().getId().equals(address.getId())) {
                    throw new BadRequestException(
                            "Electrodoméstico " + a.getId() + " no pertenece a la dirección " + address.getId(), null);
                }
                result.add(a);
            }
        }

        if (applianceDtos != null) {
            for (ApplianceRequestDto dto : applianceDtos) {

                Appliance appliance = applianceMapper.toEntity(dto, address.getId());
                
                result.add(applianceRepository.save(appliance));
            }
        }

        return result;
    }

    @Transactional
    public ApplianceResponseDto createForAddress(Long addressId, ApplianceRequestDto req) {
        
        Appliance appliance = applianceMapper.toEntity(req, addressId);

        return applianceMapper.toResponse(applianceRepository.save(appliance));

    }
    
    @Transactional
    public ApplianceResponseDto updateByAddressAndId(Long addressId, Long applianceId, ApplianceRequestDto req) {

        Appliance a = applianceMapper.update(applianceId, addressId, req);

        return applianceMapper.toResponse(applianceRepository.save(a));

    }

    @Transactional
    public ApplianceResponseDto setNotActiveById(Long id) {
        Appliance appliance = applianceRepository.findById(id).orElseThrow(() -> new NotFoundException("Electrodoméstico", id));
        appliance.setActive(false);
        return applianceMapper.toResponse(applianceRepository.save(appliance));
    }

    @Transactional(readOnly = true)
    public List<ApplianceListDto> listByAddress(Long addressId) {
        if (!addressRepository.existsById(addressId)) {
            throw new NotFoundException("Dirección", addressId);
        }
        return applianceRepository.findByAddressIdOrderByIdAsc(addressId).stream().map(ApplianceMapper::toList)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<ApplianceListDto> listAll(int pageInt) {
        return applianceRepository.findAll(pageableIdDesc(pageInt)).map(ApplianceMapper::toList);
    }

    @Transactional(readOnly = true)
    public ApplianceResponseDto getById(Long id) {
        Optional<Appliance> applianceOpt = applianceRepository.findById(id);
        return applianceMapper.toResponse(applianceOpt.orElseThrow(() -> new NotFoundException("Electrodoméstico", id)));
    }

    // HELPER
    private Pageable pageableIdDesc(int pageInt) {
        return PageRequest.of(Math.max(pageInt, 0), MAX_PAGE_SIZE, Sort.by(Sort.Order.desc("id")));
    }

}
