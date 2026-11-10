package br.com.vitalis.exceptions;

public class ConsultaNaoEncontradaException extends RecursoNaoEncontradoException {
    public ConsultaNaoEncontradaException(long id) {
        super("Consulta " + id + " não encontrada.");
    }
}
