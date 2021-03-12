package io.github.pauloviana26.clientes.model.repository;

import io.github.pauloviana26.clientes.model.entity.ServicoPrestado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServicoPrestadoRepository extends JpaRepository<ServicoPrestado, Integer> {
    @Query("SELECT s FROM ServicoPrestado s join s.cliente c" +
            " WHERE upper(c.nome) like upper(:nome) or :nome = NULL" +
            " AND MONTH(s.data) =:mes or :mes IS NULL")
    List<ServicoPrestado> findByNomeClienteAndMes(@Param("nome") String nome, @Param("mes") Integer mes);
}
