package br.com.vitalis.quartos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

@Service
public class InternacaoService {

    public record Cadastro(
        Long pacienteId,
        Long profissionalId,
        Long quartoId
    ) {}

    private final QuartoService quartoService;

    private final Map<Long, Internacao> internacoes =
        new ConcurrentHashMap<>();

    private final AtomicLong sequencia = new AtomicLong();

    public InternacaoService(QuartoService quartoService) {
        this.quartoService = quartoService;
    }

    public synchronized Internacao internar(Cadastro dados) {
        if (dados == null) {
            throw new IllegalArgumentException(
                "Informe os dados da internação.");
        }

        if (dados.pacienteId() == null ||
            dados.pacienteId() <= 0 ||
            dados.profissionalId() == null ||
            dados.profissionalId() <= 0) {
            throw new IllegalArgumentException(
                "Paciente e profissional são obrigatórios.");
        }

        boolean jaInternado = internacoes.values().stream()
            .anyMatch(i ->
                i.getPacienteId().equals(dados.pacienteId())
                && i.getStatus() == Internacao.Status.ATIVA
            );

        if (jaInternado) {
            throw new IllegalStateException(
                "Paciente já possui internação ativa.");
        }

        quartoService.ocupar(dados.quartoId());

        Internacao internacao = new Internacao(
            sequencia.incrementAndGet(),
            dados.pacienteId(),
            dados.profissionalId(),
            dados.quartoId()
        );

        internacoes.put(internacao.getId(), internacao);

        return internacao;
    }

    public List<Internacao> listar() {
        List<Internacao> resultado =
            new ArrayList<>(internacoes.values());

        resultado.sort(
            Comparator.comparing(Internacao::getId)
        );

        return resultado;
    }

    public synchronized Internacao darAlta(Long id) {
        Internacao internacao = internacoes.get(id);

        if (internacao == null) {
            throw new QuartoNaoEncontradoException(
                "Internação não encontrada.");
        }

        if (internacao.getStatus() ==
                Internacao.Status.ENCERRADA) {
            throw new IllegalStateException(
                "Esta internação já foi encerrada.");
        }

        quartoService.liberar(internacao.getQuartoId());
        internacao.encerrar();

        return internacao;
    }
}
