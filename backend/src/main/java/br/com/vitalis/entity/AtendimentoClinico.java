package br.com.vitalis.entity;

import java.time.LocalDateTime;

import br.com.vitalis.model.ConsultaClinica;
import br.com.vitalis.model.Encaminhamento;

public interface AtendimentoClinico {
    ConsultaClinica consultarRotina(long pacienteId, LocalDateTime dataHora, String motivo);

    ConsultaClinica avaliarInicialmente(long pacienteId, LocalDateTime dataHora, String motivo);

    Encaminhamento encaminhar(long pacienteId, String especialidade, String motivo);
}
