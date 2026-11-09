package br.com.vitalis.atendimentos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;

public class PrioridadeEmergenciaTest {
    @Test
    void deveTerQuatroPrioridadesDaEmergencia(){
        Set<PrioridadeEmergencia> esperadas  = Set.of(

            PrioridadeEmergencia.BAIXA,
            PrioridadeEmergencia.MODERADA,
            PrioridadeEmergencia.ALTA,
            PrioridadeEmergencia.CRITICA
        );

        assertEquals(esperadas, Set.of(PrioridadeEmergencia.values()));
    }
}
