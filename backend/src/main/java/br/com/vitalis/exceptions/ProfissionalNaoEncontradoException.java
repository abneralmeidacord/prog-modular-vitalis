package br.com.vitalis.exceptions;

public class ProfissionalNaoEncontradoException extends RecursoNaoEncontradoException {
    public ProfissionalNaoEncontradoException(long id) {
        super("Profissional " + id + " não encontrado.");
    }
}
