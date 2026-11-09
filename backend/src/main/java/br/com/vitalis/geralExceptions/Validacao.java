package br.com.vitalis.geralExceptions;

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

    public static String textoOpcional(String valor) {
        return valor == null || valor.isBlank() ? null : valor.strip();
    }

    public static <T> T obrigatorio(T valor, String campo) {
        if (valor == null) {
            throw new DadosInvalidosException("Preencha o campo " + campo + ".");
        }
        return valor;
    }

    public static String emailOpcional(String email) {
        String valor = textoOpcional(email);
        if (valor != null && !valor.matches(FORMATO_EMAIL)) {
            throw new DadosInvalidosException("E-mail inválido.");
        }
        return valor;
    }
}
