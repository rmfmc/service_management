package ruben.springboot.service_management.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ruben.springboot.service_management.errors.BadRequestException;
import ruben.springboot.service_management.errors.NotFoundException;
import ruben.springboot.service_management.models.Address;
import ruben.springboot.service_management.models.Client;
import ruben.springboot.service_management.models.dtos.lists.AddressListDto;
import ruben.springboot.service_management.models.dtos.requests.AddressRequestDto;
import ruben.springboot.service_management.models.dtos.responses.AddressResponseDto;
import ruben.springboot.service_management.models.mappers.AddressMapper;
import ruben.springboot.service_management.repositories.AddressRepository;

@Service
public class AddressService {

    @Autowired
    private AddressRepository repository;
    @Autowired
    private AddressMapper addressMapper;

    private int MAX_PAGE_SIZE = 30;

    @Transactional
    public Address resolve(Long addressId, AddressRequestDto addressDto, Client client) {

        if (client == null) {
            throw new BadRequestException("Cliente");
        }

        if (addressId != null) {
            Address addressDb = repository.findById(addressId)
                    .orElseThrow(() -> new NotFoundException("Dirección", addressId));

            if (!addressDb.getClient().getId().equals(client.getId())) {
                throw new BadRequestException("Dirección " + addressId + " no pertenece a cliente " + client.getId(), null);
            }

            if (addressDto != null) {
                addressDb = repository.save(addressMapper.update(addressDb, addressDto, client.getId()));
            }

            return addressDb;
        }

        if (addressDto == null) {
            throw new BadRequestException("Dirección", "dirección");
        }

        Address a = addressMapper.toEntity(addressDto, client.getId());

        return repository.save(a);
    }

    @Transactional
    public AddressResponseDto create(Long clientId, AddressRequestDto req) {

        Address created = repository.save(addressMapper.toEntity(req, clientId));
        return addressMapper.toResponse(created);
    }

    @Transactional
    public AddressResponseDto update(Long clientId, Long addressId, AddressRequestDto req) {

        Address addressDb = repository.findById(addressId)
                .orElseThrow(() -> new NotFoundException("Dirección", addressId));

        addressMapper.update(addressDb, req, clientId);
        return addressMapper.toResponse(repository.save(addressDb));
    }

    @Transactional
    public void delete(Long id) {

        Address address = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Dirección", id));

        repository.delete(address);
    }

    @Transactional(readOnly = true)
    public Page<AddressListDto> list(int pageInt) {
        return repository.findAll(pageableIdDesc(pageInt)).map(AddressMapper::toList);
    }

    @Transactional(readOnly = true)
    public AddressResponseDto getById(Long id) {
        return addressMapper.toResponse(repository.findById(id).orElseThrow(() -> new NotFoundException("Dirección", id)));
    }

    @Transactional(readOnly = true)
    public List<AddressListDto> listByClientId(Long clientId) {
        return repository.findByClientId(clientId).stream().map(AddressMapper::toList).toList();
    }

    // HELPER
    private Pageable pageableIdDesc(int pageInt) {
        return PageRequest.of(Math.max(pageInt, 0), MAX_PAGE_SIZE, Sort.by(Sort.Order.desc("id")));
    }

}
