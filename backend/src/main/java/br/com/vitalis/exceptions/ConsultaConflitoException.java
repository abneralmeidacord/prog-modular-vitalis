package br.com.vitalis.exceptions;

public class ConsultaConflitoException extends RuntimeException {
    public ConsultaConflitoException(String mensagem) {
        super(mensagem);
    }
}
