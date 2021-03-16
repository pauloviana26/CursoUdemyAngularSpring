package io.github.pauloviana26.agenda.model.repository;

import io.github.pauloviana26.agenda.model.entity.Contato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContatoRepository extends JpaRepository<Contato, Integer> {
}
