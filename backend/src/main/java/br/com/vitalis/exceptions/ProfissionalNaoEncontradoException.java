package br.com.vitalis.exceptions;

public class ProfissionalNaoEncontradoException extends RuntimeException {
    public ProfissionalNaoEncontradoException(long id) {
        super("Profissional " + id + " não encontrado.");
    }
}
