package br.com.vitalis.model;

import br.com.vitalis.entity.ClinicoGeral;
import br.com.vitalis.exceptions.DadosInvalidosException;

public record Encaminhamento(ClinicoGeral clinico, long pacienteId, String especialidade, String motivo) {
    public Encaminhamento {
        if (clinico == null) {
            throw new DadosInvalidosException("Clínico é obrigatório.");
        }
        Validacao.pacienteObrigatorio(pacienteId);
        especialidade = Validacao.textoObrigatorio(especialidade, "Especialidade de destino");
        motivo = Validacao.textoObrigatorio(motivo, "Motivo");
    }
}
