package br.com.vitalis.profissionais;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import br.com.vitalis.Consultas.ConsultaClinica;

class ProfissionalTest {
    private final LocalDateTime dataHora = LocalDateTime.of(2026, 11, 9, 10, 30);

    @Test
    void clinicoRealizaRotinaAvaliacaoInicialEEncaminhamento() {
        ClinicoGeral clinico = new ClinicoGeral(1, "Paula Mendes", "CRM 1", "555-0100", "paula@example.com", true);
        AtendimentoClinico atendimento = clinico;

        ConsultaClinica rotina = atendimento.consultarRotina(7, dataHora, "Acompanhamento");
        ConsultaClinica avaliacao = atendimento.avaliarInicialmente(7, dataHora, "Primeira consulta");
        Encaminhamento encaminhamento = atendimento.encaminhar(7, "Cardiologia", "Avaliação especializada");

        assertEquals(new ConsultaClinica(clinico, 7, dataHora, "Acompanhamento", ConsultaClinica.Tipo.ROTINA), rotina);
        assertEquals(ConsultaClinica.Tipo.AVALIACAO_INICIAL, avaliacao.tipo());
        assertEquals(new Encaminhamento(clinico, 7, "Cardiologia", "Avaliação especializada"), encaminhamento);
        assertThrows(DadosInvalidosException.class, () -> atendimento.encaminhar(7, " ", "Avaliação"));
        assertThrows(DadosInvalidosException.class, () -> atendimento.consultarRotina(0, dataHora, "Retorno"));
    }

    @Test
    void cirurgiaoCriaVinculoComPacienteDataHoraEDescricao() {
        Cirurgiao cirurgiao = new Cirurgiao(2, "Ana Costa", "CRM 2", "555-0101", "ana@example.com", true,
                "Cirurgia geral");
        AtuacaoCirurgica atuacao = cirurgiao;

        VinculoCirurgico vinculo = atuacao.vincularProcedimento(7, dataHora, "Apendicectomia");

        assertSame(cirurgiao, vinculo.cirurgiao());
        assertEquals(7, vinculo.pacienteId());
        assertEquals(dataHora, vinculo.dataHora());
        assertEquals("Apendicectomia", vinculo.descricao());
        assertThrows(DadosInvalidosException.class, () -> atuacao.vincularProcedimento(7, null, "Apendicectomia"));
        assertThrows(DadosInvalidosException.class, () -> atuacao.vincularProcedimento(7, dataHora, " "));
    }
}
