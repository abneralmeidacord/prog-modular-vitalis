package br.com.vitalis.Consultas;

import br.com.vitalis.profissionais.DadosInvalidosException;

final class ValidacaoConsulta {
    private ValidacaoConsulta() {
    }

    static String textoObrigatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new DadosInvalidosException("Preencha o campo " + campo + ".");
        }
        return valor.strip();
    }

    static void pacienteObrigatorio(long pacienteId) {
        if (pacienteId <= 0) {
            throw new DadosInvalidosException("Informe um ID de paciente válido.");
        }
    }
}
