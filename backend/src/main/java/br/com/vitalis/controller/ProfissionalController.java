package br.com.vitalis.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.vitalis.entity.Profissional;
import br.com.vitalis.model.CadastroProfissional;
import br.com.vitalis.service.ProfissionalService;

@RestController
@RequestMapping("/profissionais")
public class ProfissionalController {
    private final ProfissionalService service;

    public ProfissionalController(ProfissionalService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Profissional> cadastrar(@RequestBody CadastroProfissional cadastro) {
        Profissional profissional = service.cadastrar(cadastro);
        return ResponseEntity.created(URI.create("/profissionais/" + profissional.getId())).body(profissional);
    }

    @GetMapping
    public List<Profissional> listar(@RequestParam(name = "area", required = false) String area) {
        return service.listar(area);
    }

    @GetMapping("/{id}")
    public Profissional buscar(@PathVariable("id") long id) {
        return service.buscar(id);
    }
}
