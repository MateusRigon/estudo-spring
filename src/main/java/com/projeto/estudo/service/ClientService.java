package com.projeto.estudo.service;

import com.projeto.estudo.handler.DuplicateResourceException;
import com.projeto.estudo.handler.ResourceNotFoundException;
import com.projeto.estudo.model.Client;
import com.projeto.estudo.repository.ClientRepository;
import jakarta.validation.Valid;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client getClientById(int id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " not found."));
    }

    public Integer getClientIdByEmail(String email) {
        return clientRepository.findByEmail(email)
                .map(Client::getId)
                .orElseThrow(() -> new ResourceNotFoundException("Client with email " + email + " not found."));
    }

    public Client createClient(@Valid Client client) {
        clientRepository.findByEmail(client.getEmail())
                .ifPresent(existing -> {
                    throw new DuplicateResourceException("Client with email " + client.getEmail() + " already exists.");
                });

        return clientRepository.save(client);
    }

    public Client updateClient(int id, @Valid Client payload) {
        Client existingClient = getClientById(id);

        clientRepository.findByEmail(payload.getEmail())
                .filter(client -> client.getId() != id)
                .ifPresent(client -> {
                    throw new DuplicateResourceException("Client with email " + payload.getEmail() + " already exists.");
                });

        existingClient.setName(payload.getName());
        existingClient.setEmail(payload.getEmail());

        return clientRepository.save(existingClient);
    }

    public void deleteClientById(int id) {
        try {
            clientRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResourceNotFoundException("Client with id " + id + " not found.");
        }
    }

    @Transactional
    public void deleteClientByEmail(String email) {
        Client existingClient = clientRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Client with email " + email + " not found."));

        clientRepository.deleteByEmail(existingClient.getEmail());
    }
}
