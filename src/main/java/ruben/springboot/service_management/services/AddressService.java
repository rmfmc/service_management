package ruben.springboot.service_management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ruben.springboot.service_management.errors.NotFoundException;
import ruben.springboot.service_management.models.Address;
import ruben.springboot.service_management.models.Client;
import ruben.springboot.service_management.models.dtos.requests.AddressRequestDto;
import ruben.springboot.service_management.models.mappers.AddressMapper;
import ruben.springboot.service_management.repositories.AddressRepository;

@Service
public class AddressService {

    @Autowired
    private AddressRepository repository;
    @Autowired
    private AddressMapper addressMapper;

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
}
