package br.com.vitalis.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import br.com.vitalis.model.PrioridadeEmergencia;
import br.com.vitalis.model.StatusEmergencia;

public class AtendimentoEmergencial extends Atendimento {
    private final PrioridadeEmergencia prioridade;
    private StatusEmergencia status = StatusEmergencia.AGUARDANDO_AVALIACAO;
    private LocalDateTime dataHoraInicio;

    public AtendimentoEmergencial(long id, Paciente paciente, Profissional profissional,
                                  PrioridadeEmergencia prioridade) {
        super(id, paciente, profissional, null);
        this.prioridade = Objects.requireNonNull(prioridade, "Prioridade obrigatória");
    }

    public PrioridadeEmergencia getPrioridade() {
        return prioridade;
    }

    public StatusEmergencia getStatus() {
        return status;
    }

    @Override
    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void iniciar(LocalDateTime dataHora) {
        if (status != StatusEmergencia.AGUARDANDO_AVALIACAO) {
            throw new IllegalStateException("A emergência precisa estar aguardando avaliação");
        }

        dataHoraInicio = Objects.requireNonNull(dataHora, "Data e hora de início obrigatórias");
        status = StatusEmergencia.INICIADO;
    }

    public void concluir() {
        if (status != StatusEmergencia.INICIADO) {
            throw new IllegalStateException("A emergência precisa ser iniciada antes de concluir");
        }

        status = StatusEmergencia.CONCLUIDO;
    }
}
