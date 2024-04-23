package com.project.crud.clients.Clientscrud.controller;

import com.project.crud.clients.Clientscrud.dto.ClientDTO;
import com.project.crud.clients.Clientscrud.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping (value = "/clients")
public class ClientController {

    @Autowired
    private ClientService service;

    @GetMapping
    public ResponseEntity<Page<ClientDTO>> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                   @RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
                                                   @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
                                                   @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
            Page<ClientDTO> list = service.findAllPaged(pageRequest);
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
