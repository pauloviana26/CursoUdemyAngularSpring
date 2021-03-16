package io.github.pauloviana26.agenda.model.api.rest;

import io.github.pauloviana26.agenda.model.entity.Contato;
import io.github.pauloviana26.agenda.model.repository.ContatoRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Part;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contatos")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ContatoController {

    private final ContatoRepository repository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private Contato salvar(@RequestBody @Valid Contato contato) {
        return repository.save(contato);
    }

    @GetMapping
    public Page<Contato> buscarTodos(
            @RequestParam(value = "page", defaultValue = "0") Integer pagina,
            @RequestParam(value = "size", defaultValue = "10") Integer elementosPorPagina
    ) {
        Sort sort = Sort.by(Sort.Direction.ASC, "nome");
        PageRequest pageRequest = PageRequest.of(pagina, elementosPorPagina, sort);
        return repository.findAll(pageRequest);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id){
        repository.deleteById(id);
    }

    @PatchMapping("{id}/favorito")
    public void favoritar(@PathVariable Integer id) {
        Optional<Contato> contato = repository.findById(id);
        contato.ifPresent( c -> {
            boolean favorito = c.isFavorito() == Boolean.TRUE;
            c.setFavorito(!favorito);
            repository.save(c);
        });
    }

    @PutMapping("{id}/foto")
    public byte[] adicionarFoto(@PathVariable Integer id,
                                @RequestParam("foto")Part arquivo){
        Optional<Contato> contato = repository.findById(id);
        return contato.map(c -> {
          try {
              InputStream is = arquivo.getInputStream();
              byte[] bytes = new byte[(int) arquivo.getSize()];
              IOUtils.readFully(is, bytes);
              c.setFoto(bytes);
              repository.save(c);
              is.close();
              return bytes;
          } catch (IOException e) {
              return null;
          }
        }).orElse(null);
    }
}
