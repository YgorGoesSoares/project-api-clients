package com.project.crud.clients.Clientscrud.repository;

import com.project.crud.clients.Clientscrud.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
