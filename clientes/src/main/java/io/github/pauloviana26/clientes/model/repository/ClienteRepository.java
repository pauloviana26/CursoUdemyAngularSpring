package io.github.pauloviana26.clientes.model.repository;

import io.github.pauloviana26.clientes.model.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
}
