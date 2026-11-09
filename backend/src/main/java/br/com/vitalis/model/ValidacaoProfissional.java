package br.com.vitalis.model;

import br.com.vitalis.exceptions.DadosProfissionaisInvalidosException;

public final class ValidacaoProfissional {
    private ValidacaoProfissional() {
    }

    public static String textoObrigatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new DadosProfissionaisInvalidosException("Preencha o campo " + campo + ".");
        }
        return valor.strip();
    }

    public static void pacienteObrigatorio(long pacienteId) {
        if (pacienteId <= 0) {
            throw new DadosProfissionaisInvalidosException("Informe um ID de paciente válido.");
        }
    }
}
