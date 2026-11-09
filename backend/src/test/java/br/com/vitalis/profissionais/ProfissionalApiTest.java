package br.com.vitalis.profissionais;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
class ProfissionalApiTest {
        @Autowired
        private MockMvc api;

        @Test
        void cadastraClinicoComDisponibilidadeInicialEBuscaPeloId() throws Exception {
                String caminho = cadastrar("CLINICO_GERAL", "CRM 101", "")
                                .andExpect(status().isCreated())
                                .andExpect(header().exists("Location"))
                                .andExpect(jsonPath("$.categoria").value("CLINICO_GERAL"))
                                .andExpect(jsonPath("$.area").value("Clínica geral"))
                                .andExpect(jsonPath("$.disponivel").value(true))
                                .andReturn().getResponse().getHeader("Location");

                api.perform(get(caminho))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.registro").value("CRM 101"));
        }

        @Test
        void filtraPorAreaEDisponibilidadeNasCategorias() throws Exception {
                cadastrar("ESPECIALISTA", "CRM 102", ", \"especialidade\": \"Cardiologia\"")
                                .andExpect(status().isCreated());
                cadastrar("ESPECIALISTA", "CRM 103", ", \"especialidade\": \"Cardiologia\", \"disponivel\": false")
                                .andExpect(status().isCreated());
                cadastrar("CIRURGIAO", "CRM 104", ", \"areaCirurgica\": \"Cirurgia geral\"")
                                .andExpect(status().isCreated());

                api.perform(get("/profissionais").param("area", "  cardIOLOGIA  "))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(1))
                                .andExpect(jsonPath("$[0].registro").value("CRM 102"))
                                .andExpect(jsonPath("$[0].especialidade").value("Cardiologia"));
                api.perform(get("/profissionais").param("area", "Cirurgia geral"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(1))
                                .andExpect(jsonPath("$[0].categoria").value("CIRURGIAO"))
                                .andExpect(jsonPath("$[0].areaCirurgica").value("Cirurgia geral"));
                api.perform(get("/profissionais").param("area", "Neurologia"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isEmpty());
                api.perform(get("/profissionais"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[?(@.registro == 'CRM 103')].disponivel").value(false));
        }

        @Test
        void rejeitaDadosObrigatoriosAusentesPorCategoria() throws Exception {
                cadastrar("ESPECIALISTA", "CRM 105", "")
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.detail").value("Preencha o campo Especialidade."));
                cadastrar("CIRURGIAO", "CRM 106", "")
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.detail").value("Preencha o campo Área cirúrgica."));
                cadastrar("CLINICO_GERAL", " ", "")
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.detail").value("Preencha o campo Registro profissional."));
                api.perform(get("/profissionais").param("area", " "))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.detail").value("Preencha o campo Área."));
        }

        @Test
        void rejeitaRegistroDuplicadoEIdDesconhecido() throws Exception {
                cadastrar("CLINICO_GERAL", "CRM 107", "").andExpect(status().isCreated());
                cadastrar("CLINICO_GERAL", " crm 107 ", "")
                                .andExpect(status().isConflict())
                                .andExpect(jsonPath("$.detail")
                                                .value("Já existe um profissional com o registro crm 107."));
                api.perform(get("/profissionais/9999"))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.detail").value("Profissional 9999 não encontrado."));
        }

        private ResultActions cadastrar(String categoria, String registro, String camposDaCategoria) throws Exception {
                String corpo = """
                                {"categoria":"%s", "nome":"Paula Mendes", "registro":"%s",
                                 "telefone":"555-0100", "email":"paula@example.com"%s}
                                """.formatted(categoria, registro, camposDaCategoria);
                return api.perform(post("/profissionais").contentType(MediaType.APPLICATION_JSON).content(corpo));
        }
}
