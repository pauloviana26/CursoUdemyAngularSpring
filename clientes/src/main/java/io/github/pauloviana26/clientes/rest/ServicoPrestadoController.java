package io.github.pauloviana26.clientes.rest;

import io.github.pauloviana26.clientes.model.entity.Cliente;
import io.github.pauloviana26.clientes.model.entity.ServicoPrestado;
import io.github.pauloviana26.clientes.model.repository.ClienteRepository;
import io.github.pauloviana26.clientes.model.repository.ServicoPrestadoRepository;
import io.github.pauloviana26.clientes.rest.dto.ServicoPrestadoDTO;
import io.github.pauloviana26.clientes.util.BigDecimalConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/servicos-prestados")
public class ServicoPrestadoController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ServicoPrestadoRepository servicoPrestadoRepository;

    @Autowired
    private BigDecimalConverter bigDecimalConverter;

    @GetMapping
    public List<ServicoPrestado> pesquisar(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "mes", required = false) Integer mes
    ) {
        return servicoPrestadoRepository.findByNomeClienteAndMes("%" + nome + "%", mes);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServicoPrestado salvar(@RequestBody @Valid ServicoPrestadoDTO dto) {

        LocalDate data = LocalDate.parse(dto.getData(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Integer idCliente = dto.getIdCliente();

        Cliente cliente =
                clienteRepository
                    .findById(idCliente)
                    .orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cliente inexistente."));

        ServicoPrestado servicoPrestado = new ServicoPrestado();
        servicoPrestado.setDescricao(dto.getDescricao());
        servicoPrestado.setData(data);
        servicoPrestado.setCliente(cliente);
        servicoPrestado.setValor(bigDecimalConverter.converter(dto.getPreco()));

        return servicoPrestadoRepository.save(servicoPrestado);
    }
}
