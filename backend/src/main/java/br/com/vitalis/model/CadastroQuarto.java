package br.com.vitalis.model;

public record CadastroQuarto(String numero,
        String andar,
        TipoQuarto tipo,
        int capacidade) {
}
