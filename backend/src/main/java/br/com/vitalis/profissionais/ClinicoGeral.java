package br.com.vitalis.profissionais;

import java.time.LocalDateTime;
import br.com.vitalis.Consultas.ConsultaClinica;

public class ClinicoGeral extends Profissional implements AtendimentoClinico {
    public ClinicoGeral(long id, String nome, String registro, String telefone, String email, boolean disponivel) {
        super(id, nome, registro, telefone, email, disponivel);
    }

    @Override
    public Categoria getCategoria() {
        return Categoria.CLINICO_GERAL;
    }

    @Override
    public String getArea() {
        return "Clínica geral";
    }

    @Override
    public ConsultaClinica consultarRotina(long pacienteId, LocalDateTime dataHora, String motivo) {
        return new ConsultaClinica(this, pacienteId, dataHora, motivo, ConsultaClinica.Tipo.ROTINA);
    }

    @Override
    public ConsultaClinica avaliarInicialmente(long pacienteId, LocalDateTime dataHora, String motivo) {
        return new ConsultaClinica(this, pacienteId, dataHora, motivo, ConsultaClinica.Tipo.AVALIACAO_INICIAL);
    }

    @Override
    public Encaminhamento encaminhar(long pacienteId, String especialidade, String motivo) {
        return new Encaminhamento(this, pacienteId, especialidade, motivo);
    }
}
