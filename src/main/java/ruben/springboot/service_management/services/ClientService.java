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
import ruben.springboot.service_management.errors.AlreadyExistsException;
import ruben.springboot.service_management.errors.BadRequestException;
import ruben.springboot.service_management.models.Client;
import ruben.springboot.service_management.models.dtos.lists.ClientListDto;
import ruben.springboot.service_management.models.dtos.requests.ClientRequestDto;
import ruben.springboot.service_management.models.dtos.responses.ClientResponseDto;
import ruben.springboot.service_management.models.mappers.ClientMapper;
import ruben.springboot.service_management.repositories.ClientRepository;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;

    @Autowired
    private ClientMapper clientMapper;

    private int MAX_PAGE_SIZE = 30;

    @Transactional
    public ClientResponseDto create(ClientRequestDto dto) {

        checkIfPhoneAlreadyExists(dto);

        Client created = clientMapper.toEntity(dto);
        return clientMapper.toResponse(repository.save(created));
    }

    @Transactional
    public ClientResponseDto update(Long clientId, ClientRequestDto dto) {

        Client clientDb = repository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Cliente", clientId));

        dto.phone = dto.phone.trim();
        String oldPhone = clientDb.getPhone();

        if (!dto.phone.equals(oldPhone)) {
            if (repository.existsByPhoneAndIdNot(dto.phone, clientId)) {
                throw new AlreadyExistsException("Teléfono", dto.phone);
            }
        }

        clientMapper.update(dto, clientDb);
        return clientMapper.toResponse(repository.save(clientDb));
    }

    @Transactional
    public void deleteById(Long clientId){
        repository.deleteById(clientId);
    }

    @Transactional
    public Client resolveForWorkOrder(Long clientId, ClientRequestDto dto) {

        if (clientId != null) {
            Client clientDb = repository.findById(clientId)
                    .orElseThrow(() -> new NotFoundException("Cliente", clientId));

            if (dto == null){
                return clientDb;
            }

            dto.phone = dto.phone.trim();
            String oldPhone = clientDb.getPhone();

            if (!dto.phone.equals(oldPhone)) {
                if (repository.existsByPhoneAndIdNot(dto.phone, clientId)) {
                    throw new AlreadyExistsException("Teléfono", dto.phone);
                }
            }

            clientMapper.update(dto, clientDb);
            return repository.save(clientDb);
        }

        if (dto == null) {
            throw new BadRequestException("Cliente", "cliente");
        }

        checkIfPhoneAlreadyExists(dto);

        Client created = clientMapper.toEntity(dto);
        return repository.save(created);

    }

    @Transactional(readOnly = true)
    public Page<ClientListDto> listAll(int pageInt) {
        return repository.findAll(pageableCreatedAtDesc(pageInt)).map(ClientMapper::toList);
    }

    @Transactional(readOnly = true)
    public ClientResponseDto getById(Long id) {
        Client c = repository.findById(id).orElseThrow(() -> new NotFoundException("Cliente", id));
        return clientMapper.toResponse(c);
    }

    @Transactional(readOnly = true)
    public List<ClientListDto> search(String q) {
        return repository.search(q).stream().map(ClientMapper::toList).toList();
    }

    // HELPER
    @Transactional(readOnly = true)
    private void checkIfPhoneAlreadyExists(ClientRequestDto dto) {

        dto.phone = dto.phone.trim();
        if (repository.existsByPhone(dto.phone)) {
            throw new AlreadyExistsException(
                    "Télefono ya existente por cliente: " + repository.findByPhone(dto.phone).get().getName());
        }

    }

    // HELPER
    private Pageable pageableCreatedAtDesc(int pageInt) {
        return PageRequest.of(Math.max(pageInt, 0), MAX_PAGE_SIZE, Sort.by(Sort.Order.desc("createdAt")));
    }

}
