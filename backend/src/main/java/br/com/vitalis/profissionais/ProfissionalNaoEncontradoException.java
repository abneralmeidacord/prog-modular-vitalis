package br.com.vitalis.profissionais;

public class ProfissionalNaoEncontradoException extends RuntimeException {
    public ProfissionalNaoEncontradoException(long id) {
        super("Profissional " + id + " não encontrado.");
    }
}
