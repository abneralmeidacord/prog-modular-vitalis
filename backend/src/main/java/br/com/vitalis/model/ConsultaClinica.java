package br.com.vitalis.model;

import java.time.LocalDateTime;

import br.com.vitalis.entity.ClinicoGeral;
import br.com.vitalis.exceptions.DadosInvalidosException;

public record ConsultaClinica(ClinicoGeral clinico, long pacienteId, LocalDateTime dataHora, String motivo, Tipo tipo) {
    public enum Tipo {
        ROTINA, AVALIACAO_INICIAL
    }

    public ConsultaClinica {
        if (clinico == null || dataHora == null || tipo == null) {
            throw new DadosInvalidosException("Clínico, data, horário e tipo são obrigatórios.");
        }
        Validacao.pacienteObrigatorio(pacienteId);
        motivo = Validacao.textoObrigatorio(motivo, "Motivo");
    }
}
