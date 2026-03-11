package ruben.springboot.service_management.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ruben.springboot.service_management.errors.AlreadyExistsException;
import ruben.springboot.service_management.errors.NotFoundException;
import ruben.springboot.service_management.models.ApplianceType;
import ruben.springboot.service_management.models.dtos.ApplianceTypeDto;
import ruben.springboot.service_management.models.mappers.ApplianceTypeMapper;
import ruben.springboot.service_management.repositories.ApplianceTypeRepository;

@Service
public class ApplianceTypeService {

    @Autowired
    private ApplianceTypeRepository repository;

    @Transactional
    public ApplianceTypeDto create(ApplianceTypeDto dto) {
        dto.name = dto.name.trim();
        if (repository.existsByNameIgnoreCase(dto.name)) {
            throw new AlreadyExistsException("Appliance type already exists: " + dto.name);
        }

        ApplianceType applianceType = ApplianceTypeMapper.toEntity(dto);
        return ApplianceTypeMapper.toDto(repository.save(applianceType));
    }

    @Transactional
    public ApplianceTypeDto update(Long applianceTypeId, ApplianceTypeDto dto) {
        ApplianceType applianceTypeDb = repository.findById(applianceTypeId)
                .orElseThrow(() -> new NotFoundException("ApplianceType not found: " + applianceTypeId));

        dto.name = dto.name.trim();
        String oldName = applianceTypeDb.getName();

        if (!dto.name.equalsIgnoreCase(oldName) && repository.existsByNameIgnoreCase(dto.name)) {
            throw new AlreadyExistsException("Appliance type already exists: " + dto.name);
        }

        ApplianceTypeMapper.update(dto, applianceTypeDb);
        return ApplianceTypeMapper.toDto(repository.save(applianceTypeDb));
    }

    @Transactional
    public void deleteById(Long applianceTypeId) {
        ApplianceType applianceType = repository.findById(applianceTypeId)
                .orElseThrow(() -> new NotFoundException("ApplianceType not found: " + applianceTypeId));
        repository.delete(applianceType);
    }

    @Transactional(readOnly = true)
    public List<ApplianceTypeDto> listAll() {
        return repository.findAll().stream().map(ApplianceTypeMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public ApplianceTypeDto getById(Long applianceTypeId) {
        ApplianceType applianceType = repository.findById(applianceTypeId)
                .orElseThrow(() -> new NotFoundException("ApplianceType not found: " + applianceTypeId));
        return ApplianceTypeMapper.toDto(applianceType);
    }

}
