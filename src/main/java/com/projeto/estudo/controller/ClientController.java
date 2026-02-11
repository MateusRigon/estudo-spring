package com.projeto.estudo.controller;

import com.projeto.estudo.dto.ApiResponse;
import com.projeto.estudo.handler.ClientNotFoundException;
import com.projeto.estudo.model.Client;
import com.projeto.estudo.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/")
public class ClientController {

    private final ClienteService clienteService;

    @Autowired
    public ClientController(ClienteService clienteService){
        this.clienteService = clienteService;
    }

    @GetMapping("/getAllClients")
    public ResponseEntity<ApiResponse<List<Client>>> getAllClients() {
        List<Client> clientes = this.clienteService.findAllClient();
        return ResponseEntity.ok(ApiResponse.success("Lista de clientes:", clientes));
    }

    @GetMapping("/getIdByEmail/{email}")
    public ResponseEntity<ApiResponse<Integer>> getIdByEmail(@PathVariable String email) {
        Integer id = this.clienteService.getIdByEmail(email);
        return ResponseEntity.ok(ApiResponse.success("ID found:", id));
    }

    @GetMapping("/getClientById/{id}")
    public ResponseEntity<ApiResponse<Client>> getClientById(@PathVariable int id) {
        Client cliente = this.clienteService.getClientById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client not found!"));

        return ResponseEntity.ok(ApiResponse.success("Client data:", cliente));
    }

    @PostMapping("/addClient")
    public ResponseEntity<ApiResponse<Client>> addClient(@Valid @RequestBody Client client){
        Client saved = this.clienteService.addClient(client);
        return ResponseEntity.ok(ApiResponse.success("Client "+client.getEmail()+" added with success!", saved));
    }

    @DeleteMapping("/deleteClientById/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteClientById(@PathVariable int id){
        clienteService.deleteClientById(id);
        return ResponseEntity.ok(ApiResponse.success("Client "+id+" deleted with success!", null));
    }

    @DeleteMapping("/deleteClientByEmail/{email}")
    public ResponseEntity<ApiResponse<Void>> deleteClient(@PathVariable String email) {
        clienteService.deleteClientByEmail(email);
        return ResponseEntity.ok(ApiResponse.success("Client "+email+" deleted with success!", null));
    }

    @PutMapping("/updateClient/{id}")
    public ResponseEntity<ApiResponse<Client>> updateClient(
            @PathVariable int id,
            @Valid @RequestBody Client client
    ) {
        Client updated = clienteService.updateClient(id, client);
        return ResponseEntity.ok(ApiResponse.success("Changes saved!", updated));
    }
}
