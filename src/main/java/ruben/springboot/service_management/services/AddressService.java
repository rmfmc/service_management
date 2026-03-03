package ruben.springboot.service_management.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ruben.springboot.service_management.errors.NotFoundException;
import ruben.springboot.service_management.models.Address;
import ruben.springboot.service_management.models.Client;
import ruben.springboot.service_management.models.dtos.lists.AddressListDto;
import ruben.springboot.service_management.models.dtos.requests.AddressRequestDto;
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
            throw new IllegalArgumentException("client is required");
        }

        if (addressId != null) {
            Address addressDb = repository.findById(addressId)
                    .orElseThrow(() -> new NotFoundException("Address not found: " + addressId));

            if (!addressDb.getClient().getId().equals(client.getId())) {
                throw new IllegalArgumentException(
                        "Address " + addressId + " does not belong to client " + client.getId());
            }

            if (addressDto != null) {
                addressDb = repository.save(addressMapper.update(addressDto, addressDb, client.getId()));
            }

            return addressDb;
        }

        // crear
        if (addressDto == null) {
            throw new IllegalArgumentException("addressDto is required when addressId is null");
        }

        Address a = addressMapper.toEntity(addressDto, client.getId());

        return repository.save(a);
    }

    @Transactional(readOnly = true)
    public Page<AddressListDto> list(int pageInt){
        return repository.findAll(pageableIdDesc(pageInt)).map(addressMapper::toList);
    }

    @Transactional(readOnly = true)
    public List<AddressListDto> listByClientId(Long clientId){
        return repository.findByClientId(clientId).stream().map(addressMapper::toList).toList();
    }

    // HELPER
    private Pageable pageableIdDesc(int pageInt) {
        return PageRequest.of(Math.max(pageInt, 0), MAX_PAGE_SIZE, Sort.by(Sort.Order.desc("id")));
    }

}
