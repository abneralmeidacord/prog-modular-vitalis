package br.com.vitalis.profissionais;

public class RegistroDuplicadoException extends RuntimeException {
    public RegistroDuplicadoException(String registro) {
        super("Já existe um profissional com o registro " + registro + ".");
    }
}
