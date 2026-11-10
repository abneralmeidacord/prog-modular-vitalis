
package br.com.vitalis.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import br.com.vitalis.model.TipoQuarto;

class QuartoTest {

    @Test
    void deveCriarQuartoDisponivel() {
        Quarto quarto = new Quarto(
            1L, "101", "1", TipoQuarto.ENFERMARIA, 2
        );

        assertEquals(2, quarto.getDisponiveis());
        assertTrue(quarto.isDisponivel());
    }

    @Test
    void deveOcuparQuarto() {
        Quarto quarto = new Quarto(
            1L, "102", "1", TipoQuarto.ENFERMARIA, 2
        );

        quarto.ocupar();

        assertEquals(1, quarto.getOcupados());
        assertEquals(1, quarto.getDisponiveis());
    }

    @Test
    void deveImpedirSuperlotacao() {
        Quarto quarto = new Quarto(
            1L, "103", "1", TipoQuarto.ISOLAMENTO, 1
        );

        quarto.ocupar();

        assertThrows(
            IllegalStateException.class,
            quarto::ocupar
        );
    }

    @Test
    void deveLiberarLeito() {
        Quarto quarto = new Quarto(
            1L, "104", "1", TipoQuarto.UTI, 1
        );

        quarto.ocupar();
        quarto.liberar();

        assertEquals(0, quarto.getOcupados());
        assertTrue(quarto.isDisponivel());
    }

    @Test
    void deveRejeitarCapacidadeInvalida() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Quarto(
                1L, "105", "1", TipoQuarto.UTI, 3
            )
        );
    }

    @Test
    void deveImpedirLiberacaoDeQuartoVazio() {
        Quarto quarto = new Quarto(
            1L, "106", "1", TipoQuarto.ENFERMARIA, 2
        );

        assertThrows(
            IllegalStateException.class,
            quarto::liberar
        );
    }
}
