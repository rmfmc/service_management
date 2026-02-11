package ruben.springboot.service_management.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ruben.springboot.service_management.errors.NotFoundException;
import ruben.springboot.service_management.errors.AlreadyExistsException;
import ruben.springboot.service_management.models.Client;
import ruben.springboot.service_management.models.dtos.lists.ClientListDto;
import ruben.springboot.service_management.models.dtos.lists.WorkOrderListDto;
import ruben.springboot.service_management.models.dtos.requests.ClientRequestDto;
import ruben.springboot.service_management.models.dtos.responses.ClientResponseDto;
import ruben.springboot.service_management.models.mappers.ClientMapper;
import ruben.springboot.service_management.models.mappers.WorkOrderMapper;
import ruben.springboot.service_management.repositories.ClientRepository;
import ruben.springboot.service_management.repositories.WorkOrderRepository;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;

    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Transactional(readOnly = true)
    public List<ClientListDto> list() {
        return repository.findAll().stream().map(ClientMapper::toListDto).toList();
    }

    @Transactional(readOnly = true)
    public ClientResponseDto get(Long id) {
        Client c = repository.findById(id).orElseThrow(() -> new NotFoundException("Client not found"));
        return ClientMapper.toResponse(c);
    }

    @Transactional
    public ClientResponseDto create(ClientRequestDto req) {

        checkIfPhoneAlreadyExists(req);

        Client c = ClientMapper.toEntity(req);

        c = repository.save(c);
        return ClientMapper.toResponse(c);
    }

    @Transactional
    public Client createEntity(ClientRequestDto req) {

        checkIfPhoneAlreadyExists(req);

        Client c = ClientMapper.toEntity(req);

        c = repository.save(c);
        return c;
    }

    @Transactional
    public Client resolve(Long clientId, ClientRequestDto clientDto) {

        if (clientId != null) {
            Client clientDb = repository.findById(clientId)
                    .orElseThrow(() -> new NotFoundException("Client not found: " + clientId));

            // si además viene dto, actualizo el cliente
            if (clientDto != null) {
                ClientMapper.update(clientDto, clientDb);

                if (clientDb.getPhone() != null) {
                    String phone = clientDb.getPhone().trim();
                    repository.findByPhone(phone).ifPresent(other -> {
                        if (!other.getId().equals(clientDb.getId())) {
                            throw new AlreadyExistsException("phone already exists");
                        }
                    });
                }

                return repository.save(clientDb);
            }

            return clientDb;
        }

        // no viene id -> crear
        if (clientDto == null) {
            throw new IllegalArgumentException("clientDto is required when clientId is null");
        }

        if (clientDto.phone != null && repository.existsByPhone(clientDto.phone.trim())) {
            throw new AlreadyExistsException("phone already exists");
        }

        Client created = ClientMapper.toEntity(clientDto);
        return repository.save(created);
    }

    @Transactional
    public Client update(ClientRequestDto dto) {

        if (dto == null || dto.id == null) {
            throw new IllegalArgumentException("Client id is required");
        }

        Client clientDb = repository.findById(dto.id)
                .orElseThrow(() -> new NotFoundException("Client not found: " + dto.id));

        ClientMapper.update(dto, clientDb);

        if (clientDb.getPhone() != null) {
            repository.findByPhone(clientDb.getPhone())
                    .ifPresent(other -> {
                        if (!other.getId().equals(clientDb.getId())) {
                            throw new AlreadyExistsException("phone already exists");
                        }
                    });
        }

        return repository.save(clientDb);
    }

    // @Transactional(readOnly = true)
    // public List<WorkOrderListDto> findWorkOrdersByClientId(Long id) {
    // if (!repository.existsById(id)) {
    // throw new NotFoundException("client not found");
    // }

    // Client client = new Client();
    // client.setId(id);
    // return
    // workOrderRepository.findByClient(client).stream().map(WorkOrderMapper::toList).toList();

    // }

    @Transactional
    public List<ClientListDto> search(String q) {
        return repository.search(q).stream().map(ClientMapper::toListDto).toList();
    }

    @Transactional(readOnly = true)
    private void checkIfPhoneAlreadyExists(ClientRequestDto dto) {

        if (repository.existsByPhone(dto.phone)) {
            throw new AlreadyExistsException("phone already exists");
        }

    }

}
