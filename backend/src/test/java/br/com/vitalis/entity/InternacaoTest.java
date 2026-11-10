package br.com.vitalis.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import br.com.vitalis.exceptions.ConflitoException;
import br.com.vitalis.exceptions.DadosInvalidosException;
import br.com.vitalis.model.CadastroInternacao;
import br.com.vitalis.model.CadastroPaciente;
import br.com.vitalis.model.CadastroProfissional;
import br.com.vitalis.model.CadastroQuarto;
import br.com.vitalis.model.TipoQuarto;
import br.com.vitalis.service.InternacaoService;
import br.com.vitalis.service.PacienteService;
import br.com.vitalis.service.ProfissionalService;
import br.com.vitalis.service.QuartoService;

class InternacaoTest {
    private final LocalDateTime entrada = LocalDateTime.now().minusDays(2).withNano(0);
    private final Profissional paula = new ClinicoGeral(1, "Paula Mendes", "CRM 1", "555-0100",
            "paula@example.com", true);
    private final Paciente rafael = paciente(1, "Rafael Costa", "111.119.704-11");
    private final Paciente lucas = paciente(2, "Lucas Soares", "222.333.444-55");

    @Test
    void internarOcupaLeitoERegistraNoHistoricoDoPaciente() {
        Quarto quarto = quarto("204", TipoQuarto.ENFERMARIA, 2);

        Internacao internacao = internar(1, rafael, quarto, entrada.toLocalDate().plusDays(6));

        assertTrue(internacao.estaAtiva());
        assertEquals(Internacao.Status.ATIVA, internacao.getStatus());
        assertSame(rafael, internacao.getPaciente());
        assertSame(quarto, internacao.getQuarto());
        assertEquals(entrada, internacao.getDataHoraInicio());
        assertEquals(List.of("Paciente em observação"), internacao.getObservacoes());
        assertEquals(1, quarto.getOcupados());
        assertTrue(quarto.isDisponivel());
        assertTrue(rafael.isInternado());
        assertEquals(List.of(internacao), rafael.getHistoricoMedico().getInternacoesRealizadas());
        assertEquals(List.of("Paciente em observação"), rafael.getHistoricoMedico().getInformacoesRelevantes());
    }

    @Test
    void pacienteNaoTemDuasInternacoesAtivas() {
        Quarto quarto204 = quarto("204", TipoQuarto.ENFERMARIA, 2);
        Quarto quarto305 = quarto("305", TipoQuarto.ISOLAMENTO, 1);
        internar(1, rafael, quarto204, null);

        assertThrows(ConflitoException.class, () -> internar(2, rafael, quarto305, null));
        assertEquals(0, quarto305.getOcupados());
        assertEquals(1, rafael.getHistoricoMedico().getInternacoesRealizadas().size());
    }

    @Test
    void ocupacaoNaoUltrapassaCapacidade() {
        Quarto quarto = quarto("305", TipoQuarto.ISOLAMENTO, 1);
        internar(1, rafael, quarto, null);

        assertFalse(quarto.isDisponivel());
        assertThrows(ConflitoException.class, () -> internar(2, lucas, quarto, null));
        assertEquals(1, quarto.getOcupados());
        assertFalse(lucas.isInternado());
        assertTrue(lucas.getHistoricoMedico().getAtendimentos().isEmpty());
    }

    @Test
    void conferePacienteAtivoEProfissionalDisponivel() {
        Quarto quarto = quarto("101", TipoQuarto.ENFERMARIA, 2);
        rafael.definirAtivo(false);
        Profissional indisponivel = new ClinicoGeral(2, "André Freitas", "CRM 2", "555-0101",
                "andre@example.com", false);

        assertThrows(ConflitoException.class, () -> internar(1, rafael, quarto, null));
        assertThrows(ConflitoException.class, () -> Internacao.internar(2, lucas, indisponivel, quarto, entrada,
                null, null));
        assertThrows(DadosInvalidosException.class, () -> Internacao.internar(3, lucas, paula, null, entrada,
                null, null));
        assertEquals(0, quarto.getOcupados());

        internar(4, lucas, quarto, null);
        assertThrows(ConflitoException.class, () -> lucas.definirAtivo(false));
        assertTrue(lucas.isAtivo());
    }

    @Test
    void altaLiberaVagaEPreservaHistorico() {
        Quarto quarto = quarto("305", TipoQuarto.ISOLAMENTO, 1);
        Internacao primeira = internar(1, rafael, quarto, null);

        primeira.registrarAlta(entrada.plusDays(1));

        assertFalse(primeira.estaAtiva());
        assertEquals(Internacao.Status.ENCERRADA, primeira.getStatus());
        assertEquals(entrada.plusDays(1), primeira.getDataEfetivaAlta());
        assertEquals(0, quarto.getOcupados());
        assertTrue(quarto.isDisponivel());
        assertFalse(rafael.isInternado());
        assertEquals(List.of(primeira), rafael.getHistoricoMedico().getInternacoesRealizadas());
        assertThrows(ConflitoException.class, () -> primeira.registrarAlta(entrada.plusDays(1)));
        assertEquals(0, quarto.getOcupados());
        assertThrows(ConflitoException.class, () -> primeira.preverAlta(LocalDate.now()));
        assertThrows(ConflitoException.class, () -> primeira.registrarObservacao("Retorno"));

        Internacao segunda = internar(2, rafael, quarto, null);
        assertEquals(List.of(primeira, segunda), rafael.getHistoricoMedico().getInternacoesRealizadas());
        assertEquals(1, quarto.getOcupados());
    }

    @Test
    void validaDatasSemAlterarOEstado() {
        Quarto quarto = quarto("101", TipoQuarto.ENFERMARIA, 2);

        assertThrows(DadosInvalidosException.class,
                () -> internar(1, rafael, quarto, entrada.toLocalDate().minusDays(1)));
        assertEquals(0, quarto.getOcupados());
        assertTrue(rafael.getHistoricoMedico().getAtendimentos().isEmpty());

        Internacao internacao = internar(2, rafael, quarto, null);
        assertThrows(DadosInvalidosException.class, () -> internacao.preverAlta(entrada.toLocalDate().minusDays(1)));
        assertThrows(DadosInvalidosException.class, () -> internacao.registrarAlta(entrada.minusMinutes(1)));
        assertThrows(DadosInvalidosException.class, () -> internacao.registrarAlta(LocalDateTime.now().plusDays(1)));
        assertThrows(DadosInvalidosException.class, () -> internacao.registrarObservacao(" "));
        assertTrue(internacao.estaAtiva());
        assertEquals(1, quarto.getOcupados());

        internacao.preverAlta(entrada.toLocalDate().plusDays(5));
        internacao.registrarObservacao("Sem intercorrências");
        assertEquals(entrada.toLocalDate().plusDays(5), internacao.getDataPrevistaAlta());
        assertEquals(List.of("Paciente em observação", "Sem intercorrências"), internacao.getObservacoes());
    }

    // Cenários vindos de quartos/InternacaoTest, agora pelo InternacaoService unificado.

    @Test
    void deveOcuparELiberarLeitoNaAlta() {
        Servicos s = new Servicos();
        Quarto quarto = s.quarto("401", TipoQuarto.ENFERMARIA, 2);

        Internacao internacao = s.internar(s.paciente("401.000.000-01"), quarto);

        assertEquals(1, quarto.getOcupados());
        assertEquals(Internacao.Status.ATIVA, internacao.getStatus());

        s.internacoes.registrarAlta(internacao.getId(), null);

        assertEquals(0, quarto.getOcupados());
        assertEquals(Internacao.Status.ENCERRADA, internacao.getStatus());
    }

    @Test
    void deveImpedirInternacaoDuplicada() {
        Servicos s = new Servicos();
        Quarto quarto = s.quarto("402", TipoQuarto.ENFERMARIA, 2);
        long paciente = s.paciente("402.000.000-01");

        s.internar(paciente, quarto);

        assertThrows(ConflitoException.class, () -> s.internar(paciente, quarto));
    }

    @Test
    void deveImpedirInternacaoSemVagas() {
        Servicos s = new Servicos();
        Quarto quarto = s.quarto("403", TipoQuarto.ISOLAMENTO, 1);

        s.internar(s.paciente("403.000.000-01"), quarto);

        assertThrows(ConflitoException.class, () -> s.internar(s.paciente("403.000.000-02"), quarto));
    }

    @Test
    void deveImpedirAltaDuplicada() {
        Servicos s = new Servicos();
        Quarto quarto = s.quarto("404", TipoQuarto.UTI, 1);
        Internacao internacao = s.internar(s.paciente("404.000.000-01"), quarto);

        s.internacoes.registrarAlta(internacao.getId(), null);

        assertThrows(ConflitoException.class, () -> s.internacoes.registrarAlta(internacao.getId(), null));
    }

    private Internacao internar(long id, Paciente paciente, Quarto quarto, LocalDate previsao) {
        return Internacao.internar(id, paciente, paula, quarto, entrada, previsao, "Paciente em observação");
    }

    private static Quarto quarto(String numero, TipoQuarto tipo, int capacidade) {
        return new Quarto(1L, numero, "1", tipo, capacidade);
    }

    private static Paciente paciente(long id, String nome, String cpf) {
        return new Paciente(id, nome, cpf, null, null, null, null, null);
    }

    private static final class Servicos {
        final PacienteService pacientes = new PacienteService();
        final ProfissionalService profissionais = new ProfissionalService();
        final QuartoService quartos = new QuartoService();
        final InternacaoService internacoes = new InternacaoService(pacientes, profissionais, quartos);
        final long profissional = profissionais.cadastrar(new CadastroProfissional(
                Profissional.Categoria.CLINICO_GERAL, "Paula Mendes", "CRM 1", "555-0100",
                "paula@example.com", true, null, null)).getId();

        Quarto quarto(String numero, TipoQuarto tipo, int capacidade) {
            return quartos.cadastrar(new CadastroQuarto(numero, "4", tipo, capacidade));
        }

        long paciente(String cpf) {
            return pacientes.cadastrar(new CadastroPaciente("Paciente " + cpf, cpf, null, null, null,
                    null, null)).getId();
        }

        Internacao internar(long paciente, Quarto quarto) {
            return internacoes.internar(new CadastroInternacao(paciente, profissional, quarto.getId(),
                    null, null));
        }
    }
}
