package com.projeto.estudo.service;

import com.projeto.estudo.entity.Cliente;
import com.projeto.estudo.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository repository){
        this.clienteRepository = repository;
    }

    public List<Cliente> findAllClient(){
        return this.clienteRepository.findAll();
    }
}
