package br.com.vitalis.atendimentos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import br.com.vitalis.pacientes.Paciente;
import br.com.vitalis.profissionais.Cirurgiao;

class AtendimentoFluxosTest {
    private final Paciente paciente = criarPaciente();
    private final Cirurgiao cirurgiao = new Cirurgiao(
            2, "Ana Costa", "CRM 2", "555-0101", "ana@example.com", true, "Cirurgia geral");
    private final LocalDateTime prevista = LocalDateTime.of(2026, 11, 9, 10, 30);
    private final LocalDateTime efetiva = prevista.plusMinutes(15);

    @Test
    void registraCadaPrioridadeEPermiteIniciarEConcluirEmergencia() {
        for (PrioridadeEmergencia prioridade : PrioridadeEmergencia.values()) {
            var emergencia = new AtendimentoEmergencial(1, paciente, cirurgiao, prioridade);

            assertEquals(prioridade, emergencia.getPrioridade());
            assertEquals(StatusEmergencia.AGUARDANDO_AVALIACAO, emergencia.getStatus());
            assertThrows(IllegalStateException.class, emergencia::concluir);
            emergencia.iniciar(efetiva);
            assertEquals(StatusEmergencia.INICIADO, emergencia.getStatus());
            assertEquals(efetiva, emergencia.getDataHoraInicio());
            emergencia.concluir();
            assertEquals(StatusEmergencia.CONCLUIDO, emergencia.getStatus());
        }
    }

    @Test
    void rejeitaEmergenciaSemPrioridade() {
        assertThrows(NullPointerException.class,
                () -> new AtendimentoEmergencial(1, paciente, cirurgiao, null));
    }

    @Test
    void impedeConcluirCirurgiaAntesDoInicio() {
        var cirurgia = criarCirurgia();

        assertThrows(IllegalStateException.class, cirurgia::concluir);
        assertEquals(StatusCirurgia.AGENDADO, cirurgia.getStatus());
    }

    @Test
    void iniciaEConcluiCirurgiaPreservandoHorariosEVinculos() {
        var cirurgia = criarCirurgia();

        assertSame(paciente, cirurgia.getPaciente());
        assertSame(cirurgiao, cirurgia.getProfissional());
        cirurgia.iniciar(efetiva);
        assertEquals(StatusCirurgia.EM_ANDAMENTO, cirurgia.getStatus());
        assertEquals(prevista, cirurgia.getDataHoraPrevista());
        assertEquals(efetiva, cirurgia.getDataHoraEfetiva());
        cirurgia.concluir();
        assertEquals(StatusCirurgia.CONCLUIDO, cirurgia.getStatus());
    }

    @Test
    void impedeIniciarOuConcluirCirurgiaCancelada() {
        var cirurgia = criarCirurgia();
        cirurgia.cancelar();

        assertThrows(IllegalStateException.class, () -> cirurgia.iniciar(efetiva));
        assertThrows(IllegalStateException.class, cirurgia::concluir);
        assertEquals(StatusCirurgia.CANCELADO, cirurgia.getStatus());
    }

    @Test
    void preservaDescricaoTipoEObservacoesDaCirurgia() {
        var cirurgia = criarCirurgia();
        cirurgia.registrarObservacoesPreOperatorias("Jejum confirmado");
        cirurgia.iniciar(efetiva);
        cirurgia.concluir();
        cirurgia.registrarObservacoesPosOperatorias("Recuperação sem intercorrências");

        assertEquals("Apendicectomia", cirurgia.getDescricao());
        assertEquals("Geral", cirurgia.getTipo());
        assertEquals("Jejum confirmado", cirurgia.getObservacoesPreOperatorias());
        assertEquals("Recuperação sem intercorrências", cirurgia.getObservacoesPosOperatorias());
    }

    @Test
    void impedeCancelarCirurgiaEmAndamento() {
        var cirurgia = criarCirurgia();
        cirurgia.iniciar(efetiva);

        assertThrows(IllegalStateException.class, cirurgia::cancelar);
        assertEquals(StatusCirurgia.EM_ANDAMENTO, cirurgia.getStatus());
    }

    private ProcedimentoCirurgico criarCirurgia() {
        return new ProcedimentoCirurgico(1, paciente, cirurgiao, "Apendicectomia", "Geral", prevista);
    }

    private static Paciente criarPaciente() {
        return new Paciente(1, "Maria Silva", "12345678901", LocalDate.of(1990, 1, 1),
                null, null, null, null);
    }
}
