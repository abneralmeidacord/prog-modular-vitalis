package br.com.vitalis.model;

import br.com.vitalis.entity.ClinicoGeral;
import br.com.vitalis.exceptions.DadosProfissionaisInvalidosException;

public record Encaminhamento(ClinicoGeral clinico, long pacienteId, String especialidade, String motivo) {
    public Encaminhamento {
        if (clinico == null) {
            throw new DadosProfissionaisInvalidosException("Clínico é obrigatório.");
        }
        ValidacaoProfissional.pacienteObrigatorio(pacienteId);
        especialidade = ValidacaoProfissional.textoObrigatorio(especialidade, "Especialidade de destino");
        motivo = ValidacaoProfissional.textoObrigatorio(motivo, "Motivo");
    }
}
