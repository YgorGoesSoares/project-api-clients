package com.project.crud.clients.Clientscrud.repository;

import com.project.crud.clients.Clientscrud.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByCpf(String cpf);
}
