package com.project.crud.clients.Clientscrud.services;

import com.project.crud.clients.Clientscrud.dto.ClientDTO;
import com.project.crud.clients.Clientscrud.entities.Client;
import com.project.crud.clients.Clientscrud.repository.ClientRepository;
import com.project.crud.clients.Clientscrud.services.exceptions.DatabaseException;
import com.project.crud.clients.Clientscrud.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;

    @Transactional(readOnly = true)
    public Page<ClientDTO> findAllPaged(PageRequest pageRequest) {
        Page<Client> list = repository.findAll(pageRequest);
        return list.map(ClientDTO::new);
    }

    @Transactional
    public ClientDTO updateClient(Long id, ClientDTO clientDTO) {
        try{
            Client entity = repository.getReferenceById(id);
            copyDtoToEntity(clientDTO, entity);
            entity = repository.save(entity);
            return new ClientDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    @Transactional
    public ClientDTO insertClient(ClientDTO clientDTO) {
        Client entity = new Client();
        copyDtoToEntity(clientDTO, entity);
        entity = repository.save(entity);
        return new ClientDTO(entity);
    }

    public void deleteClient(Long id) {
        Optional<Client> optionalClient = repository.findById(id);
        if (!optionalClient.isPresent()) {
            throw new ResourceNotFoundException("Id not found (" + id + ")");
        } try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
    }

    private void copyDtoToEntity(ClientDTO clientDTO, Client client) {
        if (clientDTO.getName() != null) {
            client.setName(clientDTO.getName());
        }
        if (clientDTO.getIncome() != null) {
            client.setIncome(clientDTO.getIncome());
        }
        if (clientDTO.getCpf() != null) {
            client.setCpf(clientDTO.getCpf());
        }
        if (clientDTO.getChildren() != null) {
            client.setChildren(clientDTO.getChildren());
        }
        if (clientDTO.getBirthDate() != null) {
            client.setBirthDate(clientDTO.getBirthDate());
        }

    }


}
