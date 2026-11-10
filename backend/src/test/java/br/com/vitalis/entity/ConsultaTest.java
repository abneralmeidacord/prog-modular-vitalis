package br.com.vitalis.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import br.com.vitalis.exceptions.ConsultaConflitoException;
import br.com.vitalis.exceptions.DadosInvalidosException;
import br.com.vitalis.model.StatusAtendimento;

class ConsultaTest {
    private final ClinicoGeral clinico = new ClinicoGeral(
            1, "Paula Mendes", "CRM 101", "555-0100",
            "paula@example.com", true);

    private final Especialista cardiologista = new Especialista(
            2, "Helena Martins", "CRM 102", "555-0101",
            "helena@example.com", true, "Cardiologia");

    @Test
    void criaConsultaDeRotinaAgendadaComClinicoGeral() {
        ConsultaRotina consulta = new ConsultaRotina(
                7, clinico,
                LocalDate.of(2026, 11, 10),
                LocalTime.of(10, 30),
                "Acompanhamento");

        assertEquals(7, consulta.getPacienteId());
        assertSame(clinico, consulta.getProfissional());
        assertEquals(StatusAtendimento.AGENDADO, consulta.getStatus());
        assertEquals("Acompanhamento", consulta.getMotivo());
    }

    @Test
    void criaConsultaEspecializadaQuandoAreaCorresponde() {
        ConsultaEspecializada consulta = new ConsultaEspecializada(
                7, cardiologista,
                LocalDate.of(2026, 11, 10),
                LocalTime.of(11, 0),
                "Avaliação cardíaca",
                "cardiologia");

        assertSame(cardiologista, consulta.getProfissional());
        assertEquals("cardiologia", consulta.getEspecialidade());
        assertEquals(StatusAtendimento.AGENDADO, consulta.getStatus());
    }

    @Test
    void rejeitaConsultaEspecializadaComAreaIncompativel() {
        assertThrows(ConsultaConflitoException.class, () ->
                new ConsultaEspecializada(
                        7, cardiologista,
                        LocalDate.of(2026, 11, 10),
                        LocalTime.of(11, 0),
                        "Avaliação",
                        "Neurologia"));
    }

    @Test
    void permiteConfirmarIniciarEConcluirConsulta() {
        ConsultaRotina consulta = criarConsultaRotina();

        consulta.confirmar();
        assertEquals(StatusAtendimento.CONFIRMADO, consulta.getStatus());

        consulta.realizar();
        assertEquals(StatusAtendimento.EM_ANDAMENTO, consulta.getStatus());

        consulta.concluir("Paciente orientado");
        assertEquals(StatusAtendimento.CONCLUIDO, consulta.getStatus());
        assertEquals("Paciente orientado", consulta.getObservacoes());
    }

    @Test
    void permiteReagendarConsultaAindaAgendada() {
        ConsultaRotina consulta = criarConsultaRotina();
        LocalDate novaData = LocalDate.of(2026, 11, 12);
        LocalTime novoHorario = LocalTime.of(14, 0);

        consulta.reagendar(novaData, novoHorario);

        assertEquals(novaData, consulta.getData());
        assertEquals(novoHorario, consulta.getHorario());
        assertEquals(StatusAtendimento.AGENDADO, consulta.getStatus());
    }

    @Test
    void naoPermiteReagendarConsultaConcluida() {
        ConsultaRotina consulta = criarConsultaRotina();
        consulta.realizar();
        consulta.concluir("Concluída");

        assertThrows(ConsultaConflitoException.class, () ->
                consulta.reagendar(
                        LocalDate.of(2026, 11, 12),
                        LocalTime.of(14, 0)));
    }

    @Test
    void naoPermiteConcluirConsultaCancelada() {
        ConsultaRotina consulta = criarConsultaRotina();
        consulta.cancelar();

        assertThrows(ConsultaConflitoException.class, () ->
                consulta.concluir("Não deve ser registrado"));

        assertEquals(StatusAtendimento.CANCELADO, consulta.getStatus());
    }

    @Test
    void rejeitaPacienteInvalidoECamposObrigatoriosAusentes() {
        assertThrows(DadosInvalidosException.class, () ->
                new ConsultaRotina(
                        0, clinico,
                        LocalDate.of(2026, 11, 10),
                        LocalTime.of(10, 30),
                        "Acompanhamento"));

        assertThrows(DadosInvalidosException.class, () ->
                new ConsultaRotina(
                        7, clinico,
                        LocalDate.of(2026, 11, 10),
                        LocalTime.of(10, 30),
                        " "));
    }

    private ConsultaRotina criarConsultaRotina() {
        return new ConsultaRotina(
                7, clinico,
                LocalDate.of(2026, 11, 10),
                LocalTime.of(10, 30),
                "Acompanhamento");
    }
}
