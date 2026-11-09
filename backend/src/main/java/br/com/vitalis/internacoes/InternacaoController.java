package br.com.vitalis.internacoes;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internacoes")
@CrossOrigin(origins = "http://localhost:3000")
public class InternacaoController {
    private final InternacaoService service;

    public InternacaoController(InternacaoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Internacao> internar(@RequestBody InternacaoService.Cadastro cadastro) {
        Internacao internacao = service.internar(cadastro);
        return ResponseEntity.created(URI.create("/internacoes/" + internacao.getId())).body(internacao);
    }

    @GetMapping
    public List<Internacao> listar(@RequestParam(name = "ativas", required = false) Boolean ativas,
                                   @RequestParam(name = "pacienteId", required = false) Long pacienteId,
                                   @RequestParam(name = "quartoId", required = false) Long quartoId) {
        return service.listar(ativas, pacienteId, quartoId);
    }

    @GetMapping("/{id}")
    public Internacao buscar(@PathVariable("id") long id) {
        return service.buscar(id);
    }

    @PatchMapping("/{id}/previsao-alta")
    public Internacao preverAlta(@PathVariable("id") long id,
                                 @RequestBody InternacaoService.PrevisaoAlta previsao) {
        return service.preverAlta(id, previsao);
    }

    @PatchMapping("/{id}/alta")
    public Internacao registrarAlta(@PathVariable("id") long id,
                                    @RequestBody(required = false) InternacaoService.Alta alta) {
        return service.registrarAlta(id, alta);
    }

    @PostMapping("/{id}/observacoes")
    public Internacao registrarObservacao(@PathVariable("id") long id,
                                          @RequestBody InternacaoService.Observacao observacao) {
        return service.registrarObservacao(id, observacao);
    }
}
