package com.project.crud.clients.Clientscrud.factories;

import com.project.crud.clients.Clientscrud.dto.ClientDTO;
import com.project.crud.clients.Clientscrud.entities.Client;

import java.time.Instant;

public class ClientFactory {
    public static Client createNewClient(String name, String cpf, Double income, Instant birthDate, Integer children) {

        return new Client(null, name, cpf, income, birthDate, children);
    }

    public static Client createGenericClient() {
        String dateTime = "1985-05-12T00:00:00Z";
        return new Client(28L, "Nome Cliente Genérico", "12345678901", 3500.00, Instant.parse(dateTime), 2);
    }

    public static ClientDTO createNewClientDTO(Client entity) {
        return new ClientDTO(entity);
    }
}
