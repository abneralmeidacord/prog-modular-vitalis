package br.com.vitalis.quartos;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internacoes")
@CrossOrigin(origins = "http://localhost:3000")
public class InternacaoController {

    private final InternacaoService service;

    public InternacaoController(InternacaoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Internacao> internar(
            @RequestBody InternacaoService.Cadastro dados) {

        Internacao internacao = service.internar(dados);

        return ResponseEntity
            .created(URI.create(
                "/internacoes/" + internacao.getId()))
            .body(internacao);
    }

    @GetMapping
    public List<Internacao> listar() {
        return service.listar();
    }

    @PatchMapping("/{id}/alta")
    public Internacao darAlta(@PathVariable Long id) {
        return service.darAlta(id);
    }
}
