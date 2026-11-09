package br.com.vitalis.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.vitalis.model.Validacao;

public abstract class Atendimento {
    private final long id;
    private final Paciente paciente;
    private final Profissional profissional;
    private final List<String> observacoes = new CopyOnWriteArrayList<>();

    protected Atendimento(long id, Paciente paciente, Profissional profissional, String observacaoInicial) {
        this.id = id;
        this.paciente = Validacao.obrigatorio(paciente, "Paciente");
        this.profissional = Validacao.obrigatorio(profissional, "Profissional responsável");
        String observacao = Validacao.textoOpcional(observacaoInicial);
        if (observacao != null) {
            observacoes.add(observacao);
        }
    }

    public long getId() {
        return id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Profissional getProfissional() {
        return profissional;
    }

    public List<String> getObservacoes() {
        return List.copyOf(observacoes);
    }

    public void registrarObservacao(String texto) {
        observacoes.add(Validacao.textoObrigatorio(texto, "Observação"));
    }

    @JsonIgnore
    public abstract LocalDateTime getDataHoraInicio();
}
