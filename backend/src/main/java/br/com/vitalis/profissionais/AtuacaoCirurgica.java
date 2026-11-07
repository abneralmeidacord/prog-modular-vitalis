package br.com.vitalis.profissionais;

import java.time.LocalDateTime;

public interface AtuacaoCirurgica {
    VinculoCirurgico vincularProcedimento(long pacienteId, LocalDateTime dataHora, String descricao);
}
