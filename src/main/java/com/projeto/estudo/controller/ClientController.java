package com.projeto.estudo.controller;

import com.projeto.estudo.entity.Cliente;
import com.projeto.estudo.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ClientController {

    private ClienteService clienteService;

    @Autowired
    public ClientController(ClienteService clienteService){
        this.clienteService = clienteService;
    }

    @GetMapping("/getAllClients")
    public List<Cliente> getAllClients(){
        return this.clienteService.findAllClient();
    }
}
