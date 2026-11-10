package br.com.vitalis.exceptions;

public class RegistroDuplicadoException extends ConflitoException {
    public RegistroDuplicadoException(String registro) {
        super("Já existe um profissional com o registro " + registro + ".");
    }
}
