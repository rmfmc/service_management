package ruben.springboot.service_management.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ruben.springboot.service_management.errors.NotFoundException;
import ruben.springboot.service_management.models.CommonFault;
import ruben.springboot.service_management.models.dtos.CommonFaultDto;
import ruben.springboot.service_management.models.mappers.CommonFaultMapper;
import ruben.springboot.service_management.repositories.ApplianceTypeRepository;
import ruben.springboot.service_management.repositories.CommonFaultRepository;

@Service
public class CommonFaultService {

    @Autowired
    private CommonFaultRepository repository;

    @Autowired
    private ApplianceTypeRepository applianceTypeRepository;

    @Autowired
    private CommonFaultMapper commonFaultMapper;

    @Transactional
    public CommonFaultDto create(CommonFaultDto dto) {
        CommonFault created = commonFaultMapper.toEntity(dto);
        return commonFaultMapper.toDto(repository.save(created));
    }

    @Transactional
    public CommonFaultDto update(Long commonFaultId, CommonFaultDto dto) {
        CommonFault updated = commonFaultMapper.update(dto, commonFaultId);
        return commonFaultMapper.toDto(repository.save(updated));
    }

    @Transactional
    public void deleteById(Long commonFaultId) {
        CommonFault commonFault = repository.findById(commonFaultId)
                .orElseThrow(() -> new NotFoundException("Fallo común", commonFaultId));
        repository.delete(commonFault);
    }

    @Transactional(readOnly = true)
    public List<CommonFaultDto> listAll() {
        return repository.findAll().stream().map(commonFaultMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public CommonFaultDto getById(Long commonFaultId) {
        CommonFault commonFault = repository.findById(commonFaultId)
                .orElseThrow(() -> new NotFoundException("Fallo común", commonFaultId));
        return commonFaultMapper.toDto(commonFault);
    }

    @Transactional(readOnly = true)
    public List<CommonFaultDto> listByApplianceTypeId(Long applianceTypeId) {
        if (!applianceTypeRepository.existsById(applianceTypeId)) {
            throw new NotFoundException("Fallo común", applianceTypeId);
        }
        return repository.findByApplianceTypeId(applianceTypeId).stream().map(commonFaultMapper::toDto).toList();
    }

}
