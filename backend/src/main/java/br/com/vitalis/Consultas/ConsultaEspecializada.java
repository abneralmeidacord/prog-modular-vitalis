package br.com.vitalis.Consultas;

import java.time.LocalDate;
import java.time.LocalTime;

import br.com.vitalis.profissionais.DadosInvalidosException;
import br.com.vitalis.profissionais.Especialista;

public class ConsultaEspecializada extends Consulta{
    private final String especialidade;

    public ConsultaEspecializada(long pacienteId, Especialista profissional, LocalDate data, LocalTime horario, String motivo, String especialidade) {
        super(pacienteId, profissional, data, horario, motivo);

        this.especialidade =
                ValidacaoConsulta.textoObrigatorio(especialidade, "Especialidade");

        if (!profissional.getArea().equalsIgnoreCase(this.especialidade)) {
            throw new ConsultaConflitoException(
                    "A especialidade da consulta não corresponde à área do profissional.");
        }
    }

    public String getEspecialidade() {
        return especialidade;
    }

    
}
