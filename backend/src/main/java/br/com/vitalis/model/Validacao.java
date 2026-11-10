package br.com.vitalis.model;

import br.com.vitalis.exceptions.DadosInvalidosException;

public final class Validacao {
    private static final String FORMATO_EMAIL = "[^\\s@]+@[^\\s@]+\\.[^\\s@]+";

    private Validacao() {
    }

    public static String textoObrigatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new DadosInvalidosException("Preencha o campo " + campo + ".");
        }
        return valor.strip();
    }

    public static void pacienteObrigatorio(long pacienteId) {
        if (pacienteId <= 0) {
            throw new DadosInvalidosException("Informe um ID de paciente válido.");
        }
    }

    public static String textoOpcional(String valor) {
        return valor == null || valor.isBlank() ? null : valor.strip();
    }

    public static <T> T obrigatorio(T valor, String campo) {
        if (valor == null) {
            throw new DadosInvalidosException("Preencha o campo " + campo + ".");
        }
        return valor;
    }

    public static String emailObrigatorio(String email) {
        return validarEmail(textoObrigatorio(email, "E-mail"));
    }

    public static String emailOpcional(String email) {
        return validarEmail(textoOpcional(email));
    }

    private static String validarEmail(String valor) {
        if (valor != null && !valor.matches(FORMATO_EMAIL)) {
            throw new DadosInvalidosException("E-mail inválido.");
        }
        return valor;
    }
}
