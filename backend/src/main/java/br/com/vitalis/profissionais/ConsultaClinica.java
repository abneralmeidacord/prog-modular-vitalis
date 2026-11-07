package br.com.vitalis.profissionais;

import java.time.LocalDateTime;

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
