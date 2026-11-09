package br.com.vitalis.exceptions;

public class ConsultaNaoEncontradaException extends RuntimeException {
    public ConsultaNaoEncontradaException(long id) {
        super("Consulta " + id + " não encontrada.");
    }
}
