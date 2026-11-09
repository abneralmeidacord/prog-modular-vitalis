package br.com.vitalis.model;

import java.time.LocalDateTime;

import br.com.vitalis.entity.Cirurgiao;
import br.com.vitalis.exceptions.DadosProfissionaisInvalidosException;

public record VinculoCirurgico(Cirurgiao cirurgiao, long pacienteId, LocalDateTime dataHora, String descricao) {
    public VinculoCirurgico {
        if (cirurgiao == null || dataHora == null) {
            throw new DadosProfissionaisInvalidosException("Cirurgião, data e horário são obrigatórios.");
        }
        ValidacaoProfissional.pacienteObrigatorio(pacienteId);
        descricao = ValidacaoProfissional.textoObrigatorio(descricao, "Descrição do procedimento");
    }
}
