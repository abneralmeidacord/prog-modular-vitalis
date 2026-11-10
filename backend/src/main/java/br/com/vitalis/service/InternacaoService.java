package br.com.vitalis.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import br.com.vitalis.entity.Internacao;
import br.com.vitalis.entity.Paciente;
import br.com.vitalis.entity.Profissional;
import br.com.vitalis.entity.Quarto;
import br.com.vitalis.exceptions.RecursoNaoEncontradoException;
import br.com.vitalis.model.AltaInternacao;
import br.com.vitalis.model.CadastroInternacao;
import br.com.vitalis.model.ObservacaoInternacao;
import br.com.vitalis.model.PrevisaoAltaInternacao;
import br.com.vitalis.model.Validacao;

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

    public synchronized Internacao internar(CadastroInternacao cadastro) {
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

    public synchronized Internacao preverAlta(long id, PrevisaoAltaInternacao previsao) {
        Internacao internacao = buscar(id);
        internacao.preverAlta(previsao.dataPrevistaAlta());
        return internacao;
    }

    public synchronized Internacao registrarAlta(long id, AltaInternacao alta) {
        Internacao internacao = buscar(id);
        internacao.registrarAlta(alta == null || alta.dataHora() == null ? agora() : alta.dataHora());
        return internacao;
    }

    public synchronized Internacao registrarObservacao(long id, ObservacaoInternacao observacao) {
        Internacao internacao = buscar(id);
        internacao.registrarObservacao(observacao.texto());
        return internacao;
    }

    private static LocalDateTime agora() {
        return LocalDateTime.now().withNano(0);
    }
}
