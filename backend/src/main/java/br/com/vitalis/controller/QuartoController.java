package br.com.vitalis.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.vitalis.entity.Quarto;
import br.com.vitalis.model.CadastroQuarto;
import br.com.vitalis.service.QuartoService;

@RestController
@RequestMapping("/quartos")
@CrossOrigin(origins = "http://localhost:3000")
public class QuartoController {

    private final QuartoService service;

    public QuartoController(QuartoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Quarto> cadastrar(
            @RequestBody CadastroQuarto dados) {

        Quarto quarto = service.cadastrar(dados);

        return ResponseEntity
            .created(URI.create("/quartos/" + quarto.getId()))
            .body(quarto);
    }

    @GetMapping
    public List<Quarto> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Quarto buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @GetMapping("/disponiveis")
    public List<Quarto> disponiveis() {
        return service.listarDisponiveis();
    }
}
