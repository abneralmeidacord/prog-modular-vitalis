package br.com.vitalis.exceptions;

public class QuartoNaoEncontradoException
        extends RuntimeException {

    public QuartoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
