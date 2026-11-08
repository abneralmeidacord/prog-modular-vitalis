package br.com.vitalis.atendimentos;

import br.com.vitalis.pacientes.Paciente;
import br.com.vitalis.profissionais.Profissional;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class Atendimento {
    private final long id;
    private final Paciente paciente;
    private final Profissional profissional;
    private String observacoes;

    protected Atendimento(long id, Paciente paciente, Profissional profissional){
        this.id = id;
        this.paciente = Object.requireNonNull(paciente, "Paciente obrigatório");
        this.profissional = Objects.requireNonNull(profissional, "Profissional obrigatório");
        this.observacoes = "";
    }

    public long getId(){
        return id;
    }

    public Paciente getPaciente(){
        return paciente;
    }

    public Profissional getProfissional(){
        return profissional;
    }

    public String getObservacoes(){
        return observacoes;
    }

    public void registrarObservacao(String texto){
        if (texto == null || texto.isBlank()){
            throw new IllegalArgumentException("Informe o texto da observação");
        }

        observacoes = observacoes.isEmpty() ? texto : observacoes + "\n" + texto;
    }

    public abstract LocalDateTime getDataHoraInicio();
}
