package ruben.springboot.service_management.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ruben.springboot.service_management.errors.AlreadyExistsException;
import ruben.springboot.service_management.errors.NotFoundException;
import ruben.springboot.service_management.models.Brand;
import ruben.springboot.service_management.models.dtos.BrandDto;
import ruben.springboot.service_management.models.mappers.BrandMapper;
import ruben.springboot.service_management.repositories.BrandRepository;

@Service
public class BrandService {

    @Autowired
    private BrandRepository repository;
    
    @Transactional
    public BrandDto create(BrandDto dto) {
        dto.name = dto.name.trim();
        if (repository.existsByNameIgnoreCase(dto.name)) {
            throw new AlreadyExistsException("Brand already exists: " + dto.name);
        }
        Brand brand = BrandMapper.toEntity(dto);
        return BrandMapper.toDto(repository.save(brand));
    }

    @Transactional
    public BrandDto update(Long brandId, BrandDto dto) {
        Brand brandDb = repository.findById(brandId)
                .orElseThrow(() -> new NotFoundException("Brand not found: " + brandId));

        dto.name = dto.name.trim();
        String oldName = brandDb.getName();

        if (!dto.name.equalsIgnoreCase(oldName) && repository.existsByNameIgnoreCase(dto.name)) {
            throw new AlreadyExistsException("Brand already exists: " + dto.name);
        }

        BrandMapper.update(dto, brandDb);
        return BrandMapper.toDto(repository.save(brandDb));
    }

    @Transactional
    public void deleteById(Long brandId) {
        Brand brand = repository.findById(brandId)
                .orElseThrow(() -> new NotFoundException("Brand not found: " + brandId));
        repository.delete(brand);
    }

    @Transactional(readOnly = true)
    public List<BrandDto> listAll() {
        return repository.findAll().stream().map(BrandMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public BrandDto getById(Long brandId) {
        Brand brand = repository.findById(brandId)
                .orElseThrow(() -> new NotFoundException("Brand not found: " + brandId));
        return BrandMapper.toDto(brand);
    }

}
