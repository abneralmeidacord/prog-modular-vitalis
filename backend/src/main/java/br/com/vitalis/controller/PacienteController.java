package br.com.vitalis.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.vitalis.entity.Internacao;
import br.com.vitalis.entity.Paciente;
import br.com.vitalis.model.CadastroPaciente;
import br.com.vitalis.model.ContatoPaciente;
import br.com.vitalis.model.SituacaoPaciente;
import br.com.vitalis.service.PacienteService;

@RestController
@RequestMapping("/pacientes")
@CrossOrigin(origins = "http://localhost:3000")
public class PacienteController {
    private final PacienteService service;

    public PacienteController(PacienteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Paciente> cadastrar(@RequestBody CadastroPaciente cadastro) {
        Paciente paciente = service.cadastrar(cadastro);
        return ResponseEntity.created(URI.create("/pacientes/" + paciente.getId())).body(paciente);
    }

    @GetMapping
    public List<Paciente> listar(@RequestParam(name = "busca", required = false) String busca) {
        return service.listar(busca);
    }

    @GetMapping("/{id}")
    public Paciente buscar(@PathVariable("id") long id) {
        return service.buscar(id);
    }

    @PutMapping("/{id}/contato")
    public Paciente atualizarContato(@PathVariable("id") long id, @RequestBody ContatoPaciente contato) {
        return service.atualizarContato(id, contato);
    }

    @PatchMapping("/{id}/situacao")
    public Paciente definirSituacao(@PathVariable("id") long id, @RequestBody SituacaoPaciente situacao) {
        return service.definirSituacao(id, situacao);
    }

    @GetMapping("/{id}/internacoes")
    public List<Internacao> listarInternacoes(@PathVariable("id") long id) {
        return service.listarInternacoes(id);
    }
}
