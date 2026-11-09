package br.com.vitalis.exceptions;

public class DadosProfissionaisInvalidosException extends RuntimeException {
    public DadosProfissionaisInvalidosException(String mensagem) {
        super(mensagem);
    }
}
