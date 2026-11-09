package br.com.vitalis.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import br.com.vitalis.model.StatusCirurgia;

public class ProcedimentoCirurgico extends Atendimento {
    private final String descricao;
    private final String tipo;
    private final LocalDateTime dataHoraPrevista;
    private String observacoesPreOperatorias = "";
    private String observacoesPosOperatorias = "";
    private LocalDateTime dataHoraEfetiva;
    private StatusCirurgia status = StatusCirurgia.AGENDADO;

    public ProcedimentoCirurgico(long id, Paciente paciente, Cirurgiao cirurgiao,
                                 String descricao, String tipo,
                                 LocalDateTime dataHoraPrevista) {
        super(id, paciente, cirurgiao, null);
        this.descricao = validarTexto(descricao, "Descrição");
        this.tipo = validarTexto(tipo, "Tipo");
        this.dataHoraPrevista = Objects.requireNonNull(
                dataHoraPrevista, "Data e hora previstas obrigatórias");
    }

    public String getDescricao() {
        return descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public LocalDateTime getDataHoraPrevista() {
        return dataHoraPrevista;
    }

    public LocalDateTime getDataHoraEfetiva() {
        return dataHoraEfetiva;
    }

    @Override
    public LocalDateTime getDataHoraInicio() {
        return dataHoraEfetiva;
    }

    public StatusCirurgia getStatus() {
        return status;
    }

    public String getObservacoesPreOperatorias() {
        return observacoesPreOperatorias;
    }

    public String getObservacoesPosOperatorias() {
        return observacoesPosOperatorias;
    }

    public void registrarObservacoesPreOperatorias(String texto) {
        observacoesPreOperatorias = validarTexto(texto, "Observações pré-operatórias");
    }

    public void registrarObservacoesPosOperatorias(String texto) {
        observacoesPosOperatorias = validarTexto(texto, "Observações pós-operatórias");
    }

    public void iniciar(LocalDateTime dataHora) {
        if (status != StatusCirurgia.AGENDADO) {
            throw new IllegalStateException("A cirurgia precisa estar agendada para iniciar");
        }

        dataHoraEfetiva = Objects.requireNonNull(dataHora, "Data e hora efetivas obrigatórias");
        status = StatusCirurgia.EM_ANDAMENTO;
    }

    public void concluir() {
        if (status != StatusCirurgia.EM_ANDAMENTO) {
            throw new IllegalStateException("A cirurgia precisa ser iniciada antes de concluir");
        }

        status = StatusCirurgia.CONCLUIDO;
    }

    public void cancelar() {
        if (status != StatusCirurgia.AGENDADO) {
            throw new IllegalStateException("A cirurgia precisa estar agendada para cancelar");
        }

        status = StatusCirurgia.CANCELADO;
    }

    private static String validarTexto(String texto, String campo) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException(campo + " é obrigatório");
        }

        return texto;
    }
}
