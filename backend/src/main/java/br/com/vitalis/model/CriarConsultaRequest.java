package br.com.vitalis.model;

import java.time.LocalDate;
import java.time.LocalTime;

public record CriarConsultaRequest(
        TipoConsulta tipo,
        long pacienteId,
        long profissionalId,
        LocalDate data,
        LocalTime horario,
        String motivo,
        String especialidade) {
}
