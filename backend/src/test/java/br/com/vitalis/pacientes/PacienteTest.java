package br.com.vitalis.pacientes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import br.com.vitalis.geralExceptions.DadosInvalidosException;

class PacienteTest {
    @Test
    void normalizaCpfEIniciaAtivoComHistoricoVazio() {
        Paciente paciente = new Paciente(1, " Mariana Alves ", " 123.456.789-09 ", LocalDate.of(1989, 8, 12),
                "(11) 98888-2211", "mariana@example.com", " ", null);

        assertEquals("Mariana Alves", paciente.getNome());
        assertEquals("123.456.789-09", paciente.getCpf());
        assertEquals("123.456.789-09", Paciente.normalizarCpf("12345678909"));
        assertNull(paciente.getEndereco());
        assertTrue(paciente.isAtivo());
        assertFalse(paciente.isInternado());
        assertSameHistorico(paciente);
    }

    @Test
    void rejeitaNomeCpfDataEEmailInvalidos() {
        assertThrows(DadosInvalidosException.class, () -> paciente(" ", "123.456.789-09", null, null));
        assertThrows(DadosInvalidosException.class, () -> paciente("Ana", null, null, null));
        assertThrows(DadosInvalidosException.class, () -> paciente("Ana", "123.456.789", null, null));
        assertThrows(DadosInvalidosException.class, () -> paciente("Ana", "abc.def.ghi-jk", null, null));
        assertThrows(DadosInvalidosException.class,
                () -> paciente("Ana", "123.456.789-09", LocalDate.now().plusDays(1), null));
        assertThrows(DadosInvalidosException.class, () -> paciente("Ana", "123.456.789-09", null, "ana@"));
    }

    @Test
    void calculaIdadeEAtualizaContatoSemPerderDadosEmCasoDeErro() {
        Paciente paciente = paciente("Ana", "123.456.789-09", LocalDate.now().minusYears(34), "ana@example.com");

        assertEquals(34, (int) paciente.calcularIdade());
        assertNull(paciente("Bia", "987.654.321-00", null, null).calcularIdade());
        assertThrows(DadosInvalidosException.class, () -> paciente.atualizarContato("(31) 4000-0000", "invalido"));
        assertNull(paciente.getTelefone());
        assertEquals("ana@example.com", paciente.getEmail());

        paciente.atualizarContato("(31) 4000-0000", "ana.nova@example.com");
        paciente.atualizarEndereco("Rua A, 10");
        assertEquals("(31) 4000-0000", paciente.getTelefone());
        assertEquals("ana.nova@example.com", paciente.getEmail());
        assertEquals("Rua A, 10", paciente.getEndereco());
    }

    private static void assertSameHistorico(Paciente paciente) {
        assertTrue(paciente.getHistoricoMedico().getPaciente() == paciente);
        assertTrue(paciente.getHistoricoMedico().getAtendimentos().isEmpty());
    }

    private static Paciente paciente(String nome, String cpf, LocalDate nascimento, String email) {
        return new Paciente(1, nome, cpf, nascimento, null, email, null, null);
    }
}
