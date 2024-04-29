package com.project.crud.clients.Clientscrud.services;

import com.project.crud.clients.Clientscrud.dto.ClientDTO;
import com.project.crud.clients.Clientscrud.entities.Client;
import com.project.crud.clients.Clientscrud.factories.ClientFactory;
import com.project.crud.clients.Clientscrud.repository.ClientRepository;
import com.project.crud.clients.Clientscrud.services.exceptions.DatabaseException;
import com.project.crud.clients.Clientscrud.services.exceptions.ResourceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

@SpringBootTest
public class ClientServiceTests {

    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientRepository repository;

    private Client client;
    private ClientDTO clientDTO;
    Optional<Client> optionalClient;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        client = ClientFactory.createGenericClient();
        clientDTO = ClientFactory.createNewClientDTO(client);
        optionalClient = Optional.of(client);
    }

    @Test
    void findByIdShouldReturnClientWhenExists() {
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(optionalClient);

        ClientDTO response = clientService.findById(1L);
        Assertions.assertEquals(ClientDTO.class, response.getClass());
        Assertions.assertNotNull(response);
        Assertions.assertEquals(optionalClient.get().getId(), response.getId());
    }

    @Test
    void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){
        Mockito.when(repository.findById(Mockito.anyLong())).thenThrow(new ResourceNotFoundException("Id not found"));

        try {
            clientService.findById(1L);
        } catch (Exception ex) {
            Assertions.assertEquals(ResourceNotFoundException.class, ex.getClass());
        }

    }

    @Test
    void insertClientShouldPersistInformationWhenValidInformation() {
        Mockito.when(repository.save(Mockito.any())).thenReturn(client);

        ClientDTO response = clientService.insertClient(clientDTO);

        Assertions.assertEquals(client.getId(), response.getId());
        Assertions.assertNotNull(response.getId());
        Assertions.assertNotNull(response.getCpf());
        Assertions.assertNotNull(response.getName());
        Assertions.assertNotNull(response.getBirthDate());
        Assertions.assertNotNull(response.getIncome());
    }

    @Test
    void insertClientShouldThrowDatabaseExceptionWhenCpfExistsOnDatabase() {
        Mockito.when(repository.save(Mockito.any())).thenThrow(DataIntegrityViolationException.class);

        try {
            clientService.insertClient(clientDTO);
        } catch (Exception ex) {
            Assertions.assertEquals(DatabaseException.class, ex.getClass());
        }
    }


    @Test
    void insertClientShouldThrowDatabaseExceptionWhenDateAreNotComplete() {
        Mockito.when(repository.save(Mockito.any())).thenThrow(ConstraintViolationException.class);

        try {
            clientService.insertClient(clientDTO);
        } catch (Exception ex) {
            Assertions.assertEquals(DatabaseException.class, ex.getClass());
        }
    }

    @Test
    void findAllPagedShouldReturnAnListOfUsersPaged() {
        Client client1 = ClientFactory.createGenericClient();
        Client client2 = ClientFactory.createGenericClient();
        client2.setId(30L);
        Page<Client> clientPage = new PageImpl<>(Arrays.asList(client1, client2));
        Mockito.when(repository.findAll(Mockito.any(Pageable.class))).thenReturn(clientPage);

        Pageable pageable = Pageable.ofSize(2);

        Page<ClientDTO> clientPageResult = clientService.findAllPaged(pageable);

        Mockito.verify(repository, Mockito.times(1)).findAll(pageable);

        Assertions.assertEquals(2, clientPageResult.getSize());
        Assertions.assertEquals(client1.getId(), clientPageResult.getContent().get(0).getId());
        Assertions.assertEquals(client2.getId(), clientPageResult.getContent().get(1).getId());


    }

    @Test
    void deleteClientShouldDeleteClientWhenIdExists() {
        client.setId(1L);
        Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.of(client));

        clientService.deleteClient(client.getId());

        Assertions.assertFalse(repository.existsById(client.getId()));
    }

    @Test
    void deleteClientShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.empty());

        try {
            clientService.deleteClient(1L);
        } catch (Exception ex) {
            Assertions.assertEquals(ResourceNotFoundException.class, ex.getClass());
        }

    }

    @Test
    void deleteClientShouldThrowDatabaseExceptionWhenCatchDataIntegrityViolationException() {
        Mockito.when(repository.findById(Mockito.any())).thenThrow(DatabaseException.class);

        try {
            clientService.deleteClient(1L);
        } catch (Exception ex) {
            Assertions.assertEquals(DatabaseException.class, ex.getClass());
        }

    }

}