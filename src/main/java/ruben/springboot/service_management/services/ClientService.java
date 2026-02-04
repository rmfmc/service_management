package ruben.springboot.service_management.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ruben.springboot.service_management.models.Client;
import ruben.springboot.service_management.models.dto.ClientListDto;
import ruben.springboot.service_management.models.dto.ClientMapper;
import ruben.springboot.service_management.models.dto.ClientRequestDto;
import ruben.springboot.service_management.models.dto.ClientResponseDto;
import ruben.springboot.service_management.repositories.ClientRepository;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;

    @Transactional(readOnly = true)
    public List<ClientListDto> list() {
        return repository.findAll().stream().map(ClientMapper::toListDto).toList();
    }

    @Transactional(readOnly = true)
    public ClientResponseDto get(Long id) {
        Client c = repository.findById(id).orElseThrow(() -> new RuntimeException("Client not found"));
        return ClientMapper.toResponse(c);
    }

    @Transactional
    public ClientResponseDto create(ClientRequestDto req) {
        Client c = ClientMapper.toEntity(req);
        c = repository.save(c);
        return ClientMapper.toResponse(c);
    }

}
