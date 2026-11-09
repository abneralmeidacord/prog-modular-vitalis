package br.com.vitalis.entity;

import java.time.LocalDateTime;

import br.com.vitalis.model.VinculoCirurgico;

public interface AtuacaoCirurgica {
    VinculoCirurgico vincularProcedimento(long pacienteId, LocalDateTime dataHora, String descricao);
}
