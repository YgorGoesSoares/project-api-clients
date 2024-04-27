package com.project.crud.clients.Clientscrud.controller;

import com.project.crud.clients.Clientscrud.dto.ClientDTO;
import com.project.crud.clients.Clientscrud.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping (value = "/clients")
public class ClientController {

    @Autowired
    private ClientService service;

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> findById(@PathVariable Long id) {
        ClientDTO clientDTO = service.findById(id);
        return ResponseEntity.ok().body(clientDTO);
    }

    @GetMapping
    public ResponseEntity<Page<ClientDTO>> findAllPaged(Pageable pageable) {
            Page<ClientDTO> list = service.findAllPaged(pageable);
            return ResponseEntity.ok().body(list);
        }


    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient (@PathVariable Long id, @RequestBody ClientDTO clientDTO) {
    clientDTO = service.updateClient(id, clientDTO);
    return ResponseEntity.ok().body(clientDTO);

    }

    @PostMapping
    public ResponseEntity<ClientDTO> insertClient (@RequestBody ClientDTO clientDTO) {
        clientDTO = service.insertClient(clientDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(clientDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(clientDTO);
    }

    @DeleteMapping (value = "/{id}")
    public ResponseEntity deleteClient (@PathVariable Long id) {
        service.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
