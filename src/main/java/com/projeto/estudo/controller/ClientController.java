package com.projeto.estudo.controller;

import com.projeto.estudo.dto.ApiResponse;
import com.projeto.estudo.model.Client;
import com.projeto.estudo.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Client>>> getAllClients() {
        List<Client> clients = clientService.getAllClients();
        return ResponseEntity.ok(ApiResponse.success("Clients retrieved successfully.", clients));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Client>> getClientById(@PathVariable int id) {
        Client client = clientService.getClientById(id);
        return ResponseEntity.ok(ApiResponse.success("Client retrieved successfully.", client));
    }

    @GetMapping("/email/{email}/id")
    public ResponseEntity<ApiResponse<Integer>> getClientIdByEmail(@PathVariable String email) {
        Integer clientId = clientService.getClientIdByEmail(email);
        return ResponseEntity.ok(ApiResponse.success("Client id retrieved successfully.", clientId));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Client>> createClient(@Valid @RequestBody Client client) {
        Client savedClient = clientService.createClient(client);
        return ResponseEntity.ok(ApiResponse.success(
                "Client " + savedClient.getEmail() + " created successfully.",
                savedClient
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Client>> updateClient(@PathVariable int id, @Valid @RequestBody Client payload) {
        Client updatedClient = clientService.updateClient(id, payload);
        return ResponseEntity.ok(ApiResponse.success("Client updated successfully.", updatedClient));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteClientById(@PathVariable int id) {
        clientService.deleteClientById(id);
        return ResponseEntity.ok(ApiResponse.success("Client deleted successfully.", null));
    }

    @DeleteMapping("/email/{email}")
    public ResponseEntity<ApiResponse<Void>> deleteClientByEmail(@PathVariable String email) {
        clientService.deleteClientByEmail(email);
        return ResponseEntity.ok(ApiResponse.success("Client deleted successfully.", null));
    }
}
