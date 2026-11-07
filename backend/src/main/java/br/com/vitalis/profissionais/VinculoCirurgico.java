package br.com.vitalis.profissionais;

import java.time.LocalDateTime;

public record VinculoCirurgico(Cirurgiao cirurgiao, long pacienteId, LocalDateTime dataHora, String descricao) {
    public VinculoCirurgico {
        if (cirurgiao == null || dataHora == null) {
            throw new DadosInvalidosException("Cirurgião, data e horário são obrigatórios.");
        }
        Validacao.pacienteObrigatorio(pacienteId);
        descricao = Validacao.textoObrigatorio(descricao, "Descrição do procedimento");
    }
}
