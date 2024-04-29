package com.project.crud.clients.Clientscrud.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.crud.clients.Clientscrud.dto.ClientDTO;
import com.project.crud.clients.Clientscrud.factories.ClientFactory;
import com.project.crud.clients.Clientscrud.services.ClientService;
import com.project.crud.clients.Clientscrud.services.exceptions.DatabaseException;
import com.project.crud.clients.Clientscrud.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@WebMvcTest(ClientController.class)
public class ClientControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ClientService service;
    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private ClientDTO clientDTO;
    private PageImpl<ClientDTO> page;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;

        clientDTO = ClientFactory.createNewClientDTO(ClientFactory.createGenericClient());
        page = new PageImpl<>(List.of(clientDTO));

        Mockito.when(service.findAllPaged(Mockito.any())).thenReturn(page);

        Mockito.when(service.findById(existingId)).thenReturn(clientDTO);
        Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        Mockito.when(service.insertClient(Mockito.any())).thenReturn(clientDTO);

        Mockito.when(service.updateClient(Mockito.eq(existingId), Mockito.any())).thenReturn(clientDTO);
        Mockito.when(service.updateClient(Mockito.eq(nonExistingId), Mockito.any())).thenThrow(ResourceNotFoundException.class);

        Mockito.doNothing().when(service).deleteClient(existingId);
        Mockito.doThrow(ResourceNotFoundException.class).when(service).deleteClient(nonExistingId);
        Mockito.doThrow(DatabaseException.class).when(service).deleteClient(dependentId);
    }

    @Test
    public void deleteClientShouldReturnNoContentWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/clients/{id}", existingId).accept(MediaType.APPLICATION_JSON));
        result.andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    public void deleteClientShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/clients/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));
        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void insertClientShouldReturnClientDTOCreated() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(clientDTO);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/clients")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isCreated());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.cpf").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.income").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.birthDate").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.children").exists());
    }

    @Test
    public void updateShouldReturnClientDTOWhenIdExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(clientDTO);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/clients/{id}", existingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.cpf").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.income").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.birthDate").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.children").exists());

    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(clientDTO);
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/clients/{id}", nonExistingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/clients")
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void findByIdShouldReturnClientWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/clients/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.cpf").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.income").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.birthDate").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.children").exists());

    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/clients/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
