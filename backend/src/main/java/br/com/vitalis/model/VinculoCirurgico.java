package br.com.vitalis.model;

import java.time.LocalDateTime;

import br.com.vitalis.entity.Cirurgiao;
import br.com.vitalis.exceptions.DadosInvalidosException;

public record VinculoCirurgico(Cirurgiao cirurgiao, long pacienteId, LocalDateTime dataHora, String descricao) {
    public VinculoCirurgico {
        if (cirurgiao == null || dataHora == null) {
            throw new DadosInvalidosException("Cirurgião, data e horário são obrigatórios.");
        }
        Validacao.pacienteObrigatorio(pacienteId);
        descricao = Validacao.textoObrigatorio(descricao, "Descrição do procedimento");
    }
}
