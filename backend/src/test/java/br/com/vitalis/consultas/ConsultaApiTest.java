package br.com.vitalis.consultas;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ConsultaApiTest {
    @Autowired
    private MockMvc api;

    @Test
    void criaConsultaDeRotinaEConcluiAtendimento() throws Exception {
        long profissionalId = cadastrarClinico();

        String caminho = api.perform(post("/consultas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "tipo": "ROTINA",
                          "pacienteId": 7,
                          "profissionalId": %d,
                          "data": "2026-11-10",
                          "horario": "10:30",
                          "motivo": "Acompanhamento"
                        }
                        """.formatted(profissionalId)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.consulta.status").value("AGENDADA"))
                .andExpect(jsonPath("$.consulta.pacienteId").value(7))
                .andReturn()
                .getResponse()
                .getHeader("Location");

        api.perform(patch(caminho + "/confirmar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.consulta.status").value("CONFIRMADA"));

        api.perform(patch(caminho + "/realizar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.consulta.status").value("EM_ATENDIMENTO"));

        api.perform(patch(caminho + "/concluir")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"observacoes": "Paciente orientado"}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.consulta.status").value("CONCLUIDA"))
                .andExpect(jsonPath("$.consulta.observacoes")
                        .value("Paciente orientado"));

        api.perform(get(caminho))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.consulta.status").value("CONCLUIDA"));
    }

    @Test
    void aceitaConsultaEspecializadaComAreaCompativel() throws Exception {
        long profissionalId = cadastrarEspecialista();

        api.perform(post("/consultas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "tipo": "ESPECIALIZADA",
                          "pacienteId": 8,
                          "profissionalId": %d,
                          "data": "2026-11-11",
                          "horario": "11:00",
                          "motivo": "Avaliação cardíaca",
                          "especialidade": "Cardiologia"
                        }
                        """.formatted(profissionalId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.consulta.especialidade")
                        .value("Cardiologia"));
    }

    @Test
    void rejeitaModalidadeOuEspecialidadeIncompativel() throws Exception {
        long clinicoId = cadastrarClinico();

        api.perform(post("/consultas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "tipo": "ESPECIALIZADA",
                          "pacienteId": 9,
                          "profissionalId": %d,
                          "data": "2026-11-11",
                          "horario": "11:00",
                          "motivo": "Avaliação",
                          "especialidade": "Cardiologia"
                        }
                        """.formatted(clinicoId)))
                .andExpect(status().isConflict());

        long especialistaId = cadastrarEspecialista();

        api.perform(post("/consultas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "tipo": "ESPECIALIZADA",
                          "pacienteId": 10,
                          "profissionalId": %d,
                          "data": "2026-11-11",
                          "horario": "11:30",
                          "motivo": "Avaliação",
                          "especialidade": "Neurologia"
                        }
                        """.formatted(especialistaId)))
                .andExpect(status().isConflict());
    }

    @Test
    void rejeitaDadosInvalidosEIdsInexistentes() throws Exception {
        long clinicoId = cadastrarClinico();

        api.perform(post("/consultas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "tipo": "ROTINA",
                          "pacienteId": 0,
                          "profissionalId": %d,
                          "data": "2026-11-10",
                          "horario": "10:30",
                          "motivo": "Acompanhamento"
                        }
                        """.formatted(clinicoId)))
                .andExpect(status().isBadRequest());

        api.perform(post("/consultas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "tipo": "ROTINA",
                          "pacienteId": 7,
                          "profissionalId": 999999,
                          "data": "2026-11-10",
                          "horario": "10:30",
                          "motivo": "Acompanhamento"
                        }
                        """))
                .andExpect(status().isNotFound());

        api.perform(get("/consultas/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void naoPermiteConcluirConsultaCanceladaPelaApi() throws Exception {
        long clinicoId = cadastrarClinico();

        String caminho = api.perform(post("/consultas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "tipo": "ROTINA",
                          "pacienteId": 11,
                          "profissionalId": %d,
                          "data": "2026-11-12",
                          "horario": "09:00",
                          "motivo": "Retorno"
                        }
                        """.formatted(clinicoId)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        api.perform(patch(caminho + "/cancelar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.consulta.status").value("CANCELADA"));

        api.perform(patch(caminho + "/concluir")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"observacoes": "Não deve concluir"}
                        """))
                .andExpect(status().isConflict());
    }

    private long cadastrarClinico() throws Exception {
        return cadastrarProfissional(
                "CLINICO_GERAL",
                "");
    }

    private long cadastrarEspecialista() throws Exception {
        return cadastrarProfissional(
                "ESPECIALISTA",
                ", \"especialidade\": \"Cardiologia\"");
    }

    private long cadastrarProfissional(
            String categoria,
            String camposDaCategoria) throws Exception {

        String registro = "CRM " + System.nanoTime();

        String localizacao = api.perform(post("/profissionais")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "categoria": "%s",
                          "nome": "Paula Mendes",
                          "registro": "%s",
                          "telefone": "555-0100",
                          "email": "paula@example.com"%s
                        }
                        """.formatted(
                        categoria,
                        registro,
                        camposDaCategoria)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        return Long.parseLong(
                localizacao.substring(localizacao.lastIndexOf('/') + 1));
    }
}
