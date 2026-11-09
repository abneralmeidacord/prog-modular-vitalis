package br.com.vitalis.Consultas;

import java.time.LocalDate;
import java.time.LocalTime;

import br.com.vitalis.profissionais.ClinicoGeral;

public class ConsultaRotina extends Consulta {
    public ConsultaRotina(long pacienteId, ClinicoGeral profissional, LocalDate data, LocalTime horario, String motivo) {
        super(pacienteId, profissional, data, horario, motivo);
    }
}