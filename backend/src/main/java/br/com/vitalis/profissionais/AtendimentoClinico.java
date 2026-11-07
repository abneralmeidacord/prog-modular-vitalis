package br.com.vitalis.profissionais;

import java.time.LocalDateTime;

public interface AtendimentoClinico {
    ConsultaClinica consultarRotina(long pacienteId, LocalDateTime dataHora, String motivo);

    ConsultaClinica avaliarInicialmente(long pacienteId, LocalDateTime dataHora, String motivo);

    Encaminhamento encaminhar(long pacienteId, String especialidade, String motivo);
}
