package br.com.vitalis.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.vitalis.model.ConcluirConsultaRequest;
import br.com.vitalis.model.ConsultaRegistrada;
import br.com.vitalis.model.CriarConsultaRequest;
import br.com.vitalis.model.ReagendarConsultaRequest;
import br.com.vitalis.service.ConsultaService;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {
    private final ConsultaService service;

    public ConsultaController(ConsultaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ConsultaRegistrada> cadastrar(
            @RequestBody CriarConsultaRequest request) {

        ConsultaRegistrada consulta = service.cadastrar(request);
        URI localizacao = URI.create("/consultas/" + consulta.id());

        return ResponseEntity.created(localizacao).body(consulta);
    }

    @GetMapping
    public List<ConsultaRegistrada> listar(@RequestParam(name = "pacienteId", required = false) Long pacienteId) {
        return service.listar(pacienteId);
    }

    @GetMapping("/{id}")
    public ConsultaRegistrada buscar(@PathVariable("id") long id) {
        return service.buscar(id);
    }

    @PatchMapping("/{id}/confirmar")
    public ConsultaRegistrada confirmar(@PathVariable("id") long id) {
        return service.confirmar(id);
    }

    @PatchMapping("/{id}/reagendar")
    public ConsultaRegistrada reagendar(
            @PathVariable("id") long id,
            @RequestBody ReagendarConsultaRequest request) {

        return service.reagendar(id, request);
    }

    @PatchMapping("/{id}/realizar")
    public ConsultaRegistrada realizar(@PathVariable("id") long id) {
        return service.realizar(id);
    }

    @PatchMapping("/{id}/concluir")
    public ConsultaRegistrada concluir(
            @PathVariable("id") long id,
            @RequestBody ConcluirConsultaRequest request) {

        return service.concluir(id, request);
    }

    @PatchMapping("/{id}/cancelar")
    public ConsultaRegistrada cancelar(@PathVariable("id") long id) {
        return service.cancelar(id);
    }

    @PatchMapping("/{id}/nao-compareceu")
    public ConsultaRegistrada registrarNaoComparecimento(
            @PathVariable("id") long id) {

        return service.registrarNaoComparecimento(id);
    }
}
