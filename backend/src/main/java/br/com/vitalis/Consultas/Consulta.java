package br.com.vitalis.Consultas;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import br.com.vitalis.profissionais.DadosInvalidosException;
import br.com.vitalis.profissionais.Profissional;

public abstract class Consulta {
    private final long pacienteId;
    private final Profissional profissional;
    private LocalDate data;
    private LocalTime horario;
    private final String motivo;
    private StatusConsulta status;
    private String observacoes;

    protected Consulta(long pacienteId, Profissional profissional, LocalDate data, LocalTime horario, String motivo) {

        ValidacaoConsulta.pacienteObrigatorio(pacienteId);

        if (profissional == null) {
            throw new DadosInvalidosException(
                    "Profissional responsável é obrigatório.");
        }
        if (data == null) {
            throw new DadosInvalidosException("Data da consulta é obrigatória.");
        }
        if (horario == null) {
            throw new DadosInvalidosException("Horário da consulta é obrigatório.");
        }

        this.pacienteId = pacienteId;
        this.profissional = profissional;
        this.data = data;
        this.horario = horario;
        this.motivo = ValidacaoConsulta.textoObrigatorio(motivo, "Motivo");
        this.status = StatusConsulta.AGENDADA;
        this.observacoes = "";
    }

    public long getPacienteId() {
        return pacienteId;
    }

    public Profissional getProfissional() {
        return profissional;
    }

    public LocalDate getData() {
        return data;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public String getMotivo() {
        return motivo;
    }

    public StatusConsulta getStatus() {
        return status;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public LocalDateTime getDataHoraInicio() {
        return LocalDateTime.of(data, horario);
    }

    private void validarStatus(StatusConsulta... permitidos) {
        for (StatusConsulta permitido : permitidos) {
            if (status == permitido) {
                return;
            }
        }

        throw new DadosInvalidosException(
                "Operação inválida para uma consulta com status " + status + ".");
    }

    public void confirmar() {
        validarStatus(StatusConsulta.AGENDADA);
        status = StatusConsulta.CONFIRMADA;
    }

    public void reagendar(LocalDate novaData, LocalTime novoHorario) {
        validarStatus(StatusConsulta.AGENDADA, StatusConsulta.CONFIRMADA);

        if (novaData == null || novoHorario == null) {
            throw new DadosInvalidosException(
                    "Nova data e novo horário são obrigatórios.");
        }

        this.data = novaData;
        this.horario = novoHorario;
        this.status = StatusConsulta.AGENDADA;
    }

    public void realizar() {
        validarStatus(StatusConsulta.AGENDADA, StatusConsulta.CONFIRMADA);
        status = StatusConsulta.EM_ATENDIMENTO;
    }

    public void concluir(String observacoes) {
        validarStatus(StatusConsulta.EM_ATENDIMENTO);

        this.observacoes = observacoes == null ? "" : observacoes.strip();
        status = StatusConsulta.CONCLUIDA;
    }

    public void cancelar() {
        validarStatus(StatusConsulta.AGENDADA, StatusConsulta.CONFIRMADA);
        status = StatusConsulta.CANCELADA;
    }

    public void registrarNaoComparecimento() {
        validarStatus(StatusConsulta.AGENDADA, StatusConsulta.CONFIRMADA);
        status = StatusConsulta.NAO_COMPARECEU;
    }
}
