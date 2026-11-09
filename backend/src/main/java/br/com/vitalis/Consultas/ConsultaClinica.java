package br.com.vitalis.Consultas;

import java.time.LocalDateTime;

import br.com.vitalis.profissionais.ClinicoGeral;
import br.com.vitalis.profissionais.DadosInvalidosException;

public record ConsultaClinica(ClinicoGeral clinico, long pacienteId, LocalDateTime dataHora, String motivo, Tipo tipo) {
    public enum Tipo {
        ROTINA, AVALIACAO_INICIAL
    }

    public ConsultaClinica {
        if (clinico == null || dataHora == null || tipo == null) {
            throw new DadosInvalidosException("Clínico, data, horário e tipo são obrigatórios.");
        }
        ValidacaoConsulta.pacienteObrigatorio(pacienteId);
        motivo = ValidacaoConsulta.textoObrigatorio(motivo, "Motivo");
    }
}
