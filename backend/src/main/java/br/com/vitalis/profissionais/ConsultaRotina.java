package br.com.vitalis.profissionais;

import java.time.LocalDate;
import java.time.LocalTime;

public class ConsultaRotina extends Consulta {
    public ConsultaRotina(long pacienteId, ClinicoGeral profissional, LocalDate data, LocalTime horario, String motivo) {
        super(pacienteId, profissional, data, horario, motivo);
    }
}