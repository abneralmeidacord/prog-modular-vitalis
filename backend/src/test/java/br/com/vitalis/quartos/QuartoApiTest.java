
package br.com.vitalis.quartos;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class QuartoApiTest {

    @Autowired
    private MockMvc api;

    @Test
    void deveCadastrarEListarQuarto() throws Exception {
        String json = """
            {
              "numero": "TESTE-101",
              "andar": "1",
              "tipo": "ENFERMARIA",
              "capacidade": 2
            }
            """;

        api.perform(post("/quartos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.numero").value("TESTE-101"))
            .andExpect(jsonPath("$.disponiveis").value(2));

        api.perform(get("/quartos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    void deveRejeitarQuartoDuplicado() throws Exception {
        String json = """
            {
              "numero": "TESTE-202",
              "andar": "2",
              "tipo": "UTI",
              "capacidade": 1
            }
            """;

        api.perform(post("/quartos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated());

        api.perform(post("/quartos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isBadRequest());
    }

    @Test
    void deveListarQuartosDisponiveis() throws Exception {
        api.perform(get("/quartos/disponiveis"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    void deveRetornarErroParaQuartoInexistente()
            throws Exception {

        api.perform(get("/quartos/999999"))
            .andExpect(status().isNotFound());
    }
}
