package br.com.vitalis.model;

import java.time.LocalDate;

public record CadastroPaciente(String nome, String cpf, LocalDate dataNascimento, String telefone, String email,
        String endereco, String observacoes) {
}
