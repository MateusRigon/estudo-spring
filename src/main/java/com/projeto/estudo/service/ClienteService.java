package com.projeto.estudo.service;

import com.projeto.estudo.handler.ClientAlreadyExistsException;
import com.projeto.estudo.handler.ClientNotFoundException;
import com.projeto.estudo.model.Client;
import com.projeto.estudo.repository.ClientRepository;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private ClientRepository clientRepository;

    @Autowired
    public ClienteService(ClientRepository repository){
        this.clientRepository = repository;
    }

    public List<Client> findAllClient(){

        if (this.clientRepository.findAll().isEmpty()){
            throw new ClientNotFoundException("No clients found!");
        }

        return this.clientRepository.findAll();
    }

    public Optional<Client> getClientById(int id) {
        Optional<Client> existingClient = Optional.of(clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client with ID " + id + " not found!")));

        return this.clientRepository.findById(id);
    }

    public Client addClient(@Valid Client client) {
        Optional<Client> existingClient = this.clientRepository.findByEmail(client.getEmail());

        if (!existingClient.isEmpty()){
            throw new ClientAlreadyExistsException("Client with email " + client.getEmail() + " already exists!");
        }

        return this.clientRepository.save(client);
    }

    public void deleteClientById(int clientId) {
        this.clientRepository.deleteById(clientId);
    }

    @Transactional
    public void deleteClientByEmail(String email) {
        clientRepository.deleteByEmail(email);
    }

    public Client updateClient(int id, Client client) {
        Client existente = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client not found!"));

        existente.setName(client.getName());
        existente.setEmail(client.getEmail());

        return clientRepository.save(existente);
    }

    public Integer getIdByEmail(String email) {
        return this.clientRepository.findByEmail(email).get().getId();
    }
}
