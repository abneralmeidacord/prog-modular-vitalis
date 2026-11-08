package br.com.vitalis.quartos;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class InternacaoTest {

    @Test
    void deveOcuparELiberarLeitoNaAlta() {
        QuartoService quartos = new QuartoService();
        InternacaoService internacoes =
            new InternacaoService(quartos);

        Quarto quarto = quartos.cadastrar(
            new QuartoService.Cadastro(
                "401", "4", TipoQuarto.ENFERMARIA, 2
            )
        );

        Internacao internacao = internacoes.internar(
            new InternacaoService.Cadastro(
                1L, 1L, quarto.getId()
            )
        );

        assertEquals(1, quarto.getOcupados());
        assertEquals(
            Internacao.Status.ATIVA,
            internacao.getStatus()
        );

        internacoes.darAlta(internacao.getId());

        assertEquals(0, quarto.getOcupados());
        assertEquals(
            Internacao.Status.ENCERRADA,
            internacao.getStatus()
        );
    }

    @Test
    void deveImpedirInternacaoDuplicada() {
        QuartoService quartos = new QuartoService();
        InternacaoService internacoes =
            new InternacaoService(quartos);

        Quarto quarto = quartos.cadastrar(
            new QuartoService.Cadastro(
                "402", "4", TipoQuarto.ENFERMARIA, 2
            )
        );

        var dados = new InternacaoService.Cadastro(
            1L, 1L, quarto.getId()
        );

        internacoes.internar(dados);

        assertThrows(
            IllegalStateException.class,
            () -> internacoes.internar(dados)
        );
    }

    @Test
    void deveImpedirInternacaoSemVagas() {
        QuartoService quartos = new QuartoService();
        InternacaoService internacoes =
            new InternacaoService(quartos);

        Quarto quarto = quartos.cadastrar(
            new QuartoService.Cadastro(
                "403", "4", TipoQuarto.ISOLAMENTO, 1
            )
        );

        internacoes.internar(
            new InternacaoService.Cadastro(
                1L, 1L, quarto.getId()
            )
        );

        assertThrows(
            IllegalStateException.class,
            () -> internacoes.internar(
                new InternacaoService.Cadastro(
                    2L, 1L, quarto.getId()
                )
            )
        );
    }

    @Test
    void deveImpedirAltaDuplicada() {
        QuartoService quartos = new QuartoService();
        InternacaoService internacoes =
            new InternacaoService(quartos);

        Quarto quarto = quartos.cadastrar(
            new QuartoService.Cadastro(
                "404", "4", TipoQuarto.UTI, 1
            )
        );

        Internacao internacao = internacoes.internar(
            new InternacaoService.Cadastro(
                1L, 1L, quarto.getId()
            )
        );

        internacoes.darAlta(internacao.getId());

        assertThrows(
            IllegalStateException.class,
            () -> internacoes.darAlta(internacao.getId())
        );
    }
}
