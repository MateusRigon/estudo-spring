package com.projeto.estudo.repository;

import com.projeto.estudo.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    Cliente findByNameAndEmail(String name, String email);
    Optional<Cliente> findByEmailAndName(String name, String email);
}
