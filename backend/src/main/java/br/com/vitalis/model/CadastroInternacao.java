package br.com.vitalis.model;

import java.time.LocalDate;

public record CadastroInternacao(Long pacienteId, Long profissionalId, Long quartoId, LocalDate dataPrevistaAlta,
        String observacoes) {
}
