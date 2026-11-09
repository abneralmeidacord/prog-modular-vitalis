package br.com.vitalis.internacoes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import br.com.vitalis.geralExceptions.RecursoNaoEncontradoException;
import br.com.vitalis.geralExceptions.Validacao;
import br.com.vitalis.pacientes.Paciente;
import br.com.vitalis.pacientes.PacienteService;
import br.com.vitalis.profissionais.Profissional;
import br.com.vitalis.profissionais.ProfissionalService;
import br.com.vitalis.quartos.Quarto;
import br.com.vitalis.quartos.QuartoService;

@Service
public class InternacaoService {
    private final Map<Long, Internacao> internacoes = new LinkedHashMap<>();
    private final PacienteService pacientes;
    private final ProfissionalService profissionais;
    private final QuartoService quartos;
    private long proximoId = 1;

    public InternacaoService(PacienteService pacientes, ProfissionalService profissionais, QuartoService quartos) {
        this.pacientes = pacientes;
        this.profissionais = profissionais;
        this.quartos = quartos;
    }

    public synchronized Internacao internar(Cadastro cadastro) {
        Paciente paciente = pacientes.buscar(Validacao.obrigatorio(cadastro.pacienteId(), "Paciente"));
        Profissional profissional = profissionais.buscar(
                Validacao.obrigatorio(cadastro.profissionalId(), "Profissional responsável"));
        Quarto quarto = quartos.buscar(Validacao.obrigatorio(cadastro.quartoId(), "Quarto"));
        Internacao internacao = Internacao.internar(proximoId, paciente, profissional, quarto, agora(),
                cadastro.dataPrevistaAlta(), cadastro.observacoes());
        internacoes.put(proximoId++, internacao);
        return internacao;
    }

    public synchronized List<Internacao> listar(Boolean ativas, Long pacienteId, Long quartoId) {
        return internacoes.values().stream()
                .filter(internacao -> ativas == null || internacao.estaAtiva() == ativas)
                .filter(internacao -> pacienteId == null || internacao.getPaciente().getId() == pacienteId)
                .filter(internacao -> quartoId == null || internacao.getQuarto().getId().equals(quartoId))
                .toList();
    }

    public synchronized Internacao buscar(long id) {
        Internacao internacao = internacoes.get(id);
        if (internacao == null) {
            throw new RecursoNaoEncontradoException("Internação " + id + " não encontrada.");
        }
        return internacao;
    }

    public synchronized Internacao preverAlta(long id, PrevisaoAlta previsao) {
        Internacao internacao = buscar(id);
        internacao.preverAlta(previsao.dataPrevistaAlta());
        return internacao;
    }

    public synchronized Internacao registrarAlta(long id, Alta alta) {
        Internacao internacao = buscar(id);
        internacao.registrarAlta(alta == null || alta.dataHora() == null ? agora() : alta.dataHora());
        return internacao;
    }

    public synchronized Internacao registrarObservacao(long id, Observacao observacao) {
        Internacao internacao = buscar(id);
        internacao.registrarObservacao(observacao.texto());
        return internacao;
    }

    private static LocalDateTime agora() {
        return LocalDateTime.now().withNano(0);
    }

    public record Cadastro(Long pacienteId, Long profissionalId, Long quartoId, LocalDate dataPrevistaAlta,
            String observacoes) {
    }

    public record PrevisaoAlta(LocalDate dataPrevistaAlta) {
    }

    public record Alta(LocalDateTime dataHora) {
    }

    public record Observacao(String texto) {
    }
}
