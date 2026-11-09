package br.com.vitalis.Consultas;

public class ConsultaConflitoException extends RuntimeException {
    public ConsultaConflitoException(String mensagem) {
        super(mensagem);
    }
}