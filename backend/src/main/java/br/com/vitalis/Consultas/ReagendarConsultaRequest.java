package br.com.vitalis.Consultas;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReagendarConsultaRequest(
        LocalDate data,
        LocalTime horario) {
}