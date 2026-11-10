package br.com.vitalis.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.vitalis.exceptions.ConsultaConflitoException;
import br.com.vitalis.exceptions.DadosInvalidosException;
import br.com.vitalis.model.StatusAtendimento;
import br.com.vitalis.model.Validacao;

public abstract class Consulta {
    private final long pacienteId;
    private final Profissional profissional;
    private LocalDate data;
    private LocalTime horario;
    private final String motivo;
    private StatusAtendimento status;
    private String observacoes;

    protected Consulta(long pacienteId, Profissional profissional, LocalDate data, LocalTime horario, String motivo) {

        Validacao.pacienteObrigatorio(pacienteId);

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
        this.motivo = Validacao.textoObrigatorio(motivo, "Motivo");
        this.status = StatusAtendimento.AGENDADO;
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

    @JsonIgnore
    public StatusAtendimento getStatus() {
        return status;
    }

    @JsonGetter("status")
    public String statusParaApi() {
        return switch (status) {
            case AGENDADO -> "AGENDADA";
            case CONFIRMADO -> "CONFIRMADA";
            case EM_ANDAMENTO -> "EM_ATENDIMENTO";
            case CONCLUIDO -> "CONCLUIDA";
            case CANCELADO -> "CANCELADA";
            default -> status.name();
        };
    }

    public String getObservacoes() {
        return observacoes;
    }

    public LocalDateTime getDataHoraInicio() {
        return LocalDateTime.of(data, horario);
    }

    private void validarStatus(StatusAtendimento... permitidos) {
        for (StatusAtendimento permitido : permitidos) {
            if (status == permitido) {
                return;
            }
        }

        throw new ConsultaConflitoException(
                "Operação inválida para uma consulta com status " + statusParaApi() + ".");
    }

    public void confirmar() {
        validarStatus(StatusAtendimento.AGENDADO);
        status = StatusAtendimento.CONFIRMADO;
    }

    public void reagendar(LocalDate novaData, LocalTime novoHorario) {
        validarStatus(StatusAtendimento.AGENDADO, StatusAtendimento.CONFIRMADO);

        if (novaData == null || novoHorario == null) {
            throw new DadosInvalidosException(
                    "Nova data e novo horário são obrigatórios.");
        }

        this.data = novaData;
        this.horario = novoHorario;
        this.status = StatusAtendimento.AGENDADO;
    }

    public void realizar() {
        validarStatus(StatusAtendimento.AGENDADO, StatusAtendimento.CONFIRMADO);
        status = StatusAtendimento.EM_ANDAMENTO;
    }

    public void concluir(String observacoes) {
        validarStatus(StatusAtendimento.EM_ANDAMENTO);

        this.observacoes = observacoes == null ? "" : observacoes.strip();
        status = StatusAtendimento.CONCLUIDO;
    }

    public void cancelar() {
        validarStatus(StatusAtendimento.AGENDADO, StatusAtendimento.CONFIRMADO);
        status = StatusAtendimento.CANCELADO;
    }

    public void registrarNaoComparecimento() {
        validarStatus(StatusAtendimento.AGENDADO, StatusAtendimento.CONFIRMADO);
        status = StatusAtendimento.NAO_COMPARECEU;
    }
}
