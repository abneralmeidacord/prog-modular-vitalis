package br.com.vitalis.Consultas;

public class ConsultaNaoEncontradaException extends RuntimeException {
    public ConsultaNaoEncontradaException(long id) {
        super("Consulta " + id + " não encontrada.");
    }
}
