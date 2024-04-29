package com.project.crud.clients.Clientscrud.repositories;

import com.project.crud.clients.Clientscrud.entities.Client;
import com.project.crud.clients.Clientscrud.factories.ClientFactory;
import com.project.crud.clients.Clientscrud.repository.ClientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ClientRepositoryTests {

    @Autowired
    private ClientRepository repository;

    private Long existingId;
    private Long notExistingId;
    private Long countTotalClients;
    private String existingCpf;
    private String notExistingCpf;

    @BeforeEach
    void setUp() throws Exception {
        this.existingId = 1L;
        this.notExistingId = 100L;
        this.countTotalClients = repository.count();
        this.existingCpf = "12345678901";
        this.notExistingCpf = "03189231029";
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
        Client client = ClientFactory.createGenericClient();
        client.setId(null);
        client.setCpf("93021029302");

        client = repository.save(client);
        Optional<Client> result = repository.findById(client.getId());

        Assertions.assertNotNull(client.getId());
        Assertions.assertEquals(countTotalClients  + 1L, client.getId());
        Assertions.assertTrue(result.isPresent());
        Assertions.assertSame(result.get(), client);
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {
        repository.deleteById(existingId);
        Assertions.assertTrue(!repository.existsById(existingId));
    }

    @Test
    public void deleteShouldNotDoAnythingWhenIdDoesNotExist() {
        repository.deleteById(notExistingId);
        Long updatedCount = repository.count();

        Assertions.assertEquals(updatedCount, countTotalClients);
    }

    @Test
    public void findByIdShouldReturnAOptionalClientNotNullWhenIdExists() {
        Optional<Client> entity = repository.findById(existingId);

        Assertions.assertTrue(entity.isPresent());
    }

    @Test
    public void findByIdShouldReturnAEmptyOptionalProductWhenIdDoesNotExists() {
        Optional<Client> entity = repository.findById(notExistingId);
        Assertions.assertTrue(entity.isEmpty());
    }

    @Test
    public void findByCpfShouldReturnAOptionalClientWhenCpfExists() {
        Optional<Client> entity = repository.findByCpf(existingCpf);
        Assertions.assertTrue(entity.isPresent());
    }

    @Test
    public void findByCpfShouldReturnAEmptyOptionalProductWhenIdDoesNotExists() {
        Optional<Client> entity = repository.findByCpf(notExistingCpf);
        Assertions.assertTrue(entity.isEmpty());
    }

}
