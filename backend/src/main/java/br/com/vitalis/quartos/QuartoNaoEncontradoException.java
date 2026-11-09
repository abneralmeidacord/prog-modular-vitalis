package br.com.vitalis.quartos;

public class QuartoNaoEncontradoException
        extends RuntimeException {

    public QuartoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
