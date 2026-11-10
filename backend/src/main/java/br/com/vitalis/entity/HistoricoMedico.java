package br.com.vitalis.entity;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import br.com.vitalis.exceptions.DadosInvalidosException;
import br.com.vitalis.model.Validacao;

public class HistoricoMedico {
    private final Paciente paciente;
    private final List<Atendimento> atendimentos = new CopyOnWriteArrayList<>();

    HistoricoMedico(Paciente paciente) {
        this.paciente = paciente;
    }

    public long getId() {
        return paciente.getId();
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void adicionarAtendimento(Atendimento atendimento) {
        Validacao.obrigatorio(atendimento, "Atendimento");
        if (atendimento.getPaciente() != paciente) {
            throw new DadosInvalidosException("O atendimento pertence a outro paciente.");
        }
        if (!atendimentos.contains(atendimento)) {
            atendimentos.add(atendimento);
        }
    }

    public List<Atendimento> getAtendimentos() {
        return List.copyOf(atendimentos);
    }

    public List<Internacao> getInternacoesRealizadas() {
        return atendimentos.stream()
                .filter(Internacao.class::isInstance)
                .map(Internacao.class::cast)
                .toList();
    }

    public Optional<Internacao> getInternacaoAtiva() {
        return getInternacoesRealizadas().stream().filter(Internacao::estaAtiva).findFirst();
    }

    public List<String> getInformacoesRelevantes() {
        return atendimentos.stream().flatMap(atendimento -> atendimento.getObservacoes().stream()).toList();
    }
}
