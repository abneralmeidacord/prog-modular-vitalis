package br.com.vitalis.internacoes;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
class InternacaoApiTest {
    @Autowired
    private MockMvc api;

    @Test
    void internacaoOcupaLeitoAparecenaFichaEAltaLiberaVaga() throws Exception {
        long profissional = profissional("CRM 201", true);
        long rafael = paciente("Rafael Costa", "119.704.000-01");
        long lucas = paciente("Lucas Soares", "119.704.000-02");
        long quarto = quarto("901", 1);

        String caminho = internar(rafael, profissional, quarto, ", \"observacoes\": \"Em observação\"")
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("ATIVA"))
                .andExpect(jsonPath("$.ativa").value(true))
                .andExpect(jsonPath("$.paciente.id").value(rafael))
                .andExpect(jsonPath("$.paciente.internado").value(true))
                .andExpect(jsonPath("$.profissional.registro").value("CRM 201"))
                .andExpect(jsonPath("$.quarto.numero").value("901"))
                .andExpect(jsonPath("$.quarto.ocupados").value(1))
                .andExpect(jsonPath("$.quarto.disponivel").value(false))
                .andExpect(jsonPath("$.observacoes[0]").value("Em observação"))
                .andExpect(jsonPath("$.dataEfetivaAlta").isEmpty())
                .andReturn().getResponse().getHeader("Location");

        api.perform(get("/quartos/disponiveis"))
                .andExpect(jsonPath("$[?(@.numero == '901')]").isEmpty());
        internar(lucas, profissional, quarto, "")
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("O quarto 901 não tem leito disponível."));
        internar(rafael, profissional, quarto("902", 2), "")
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("O paciente Rafael Costa já possui uma internação ativa."));
        api.perform(patch("/pacientes/" + rafael + "/situacao").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ativo\": false}"))
                .andExpect(status().isConflict());
        api.perform(get("/pacientes/" + rafael + "/internacoes"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].ativa").value(true));

        api.perform(patch(caminho + "/alta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ENCERRADA"))
                .andExpect(jsonPath("$.dataEfetivaAlta").isNotEmpty())
                .andExpect(jsonPath("$.quarto.ocupados").value(0));
        api.perform(patch(caminho + "/alta"))
                .andExpect(status().isConflict());
        api.perform(get("/quartos/" + quarto))
                .andExpect(jsonPath("$.ocupados").value(0))
                .andExpect(jsonPath("$.disponivel").value(true));
        api.perform(get("/pacientes/" + rafael))
                .andExpect(jsonPath("$.internado").value(false));
        api.perform(get("/pacientes/" + rafael + "/internacoes"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].ativa").value(false));
        api.perform(get("/internacoes").param("quartoId", String.valueOf(quarto)).param("ativas", "false"))
                .andExpect(jsonPath("$.length()").value(1));
        api.perform(get("/internacoes").param("pacienteId", String.valueOf(rafael)).param("ativas", "true"))
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void atualizaPrevisaoEObservacoesSomenteEnquantoAtiva() throws Exception {
        long profissional = profissional("CRM 202", true);
        long paciente = paciente("Aline Previsao", "119.704.000-03");
        String caminho = internar(paciente, profissional, quarto("903", 2), "")
                .andReturn().getResponse().getHeader("Location");
        String amanha = LocalDate.now().plusDays(1).toString();

        api.perform(patch(caminho + "/previsao-alta").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"dataPrevistaAlta\": \"" + amanha + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dataPrevistaAlta").value(amanha));
        api.perform(patch(caminho + "/previsao-alta").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"dataPrevistaAlta\": \"2000-01-01\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("A previsão de alta não pode ser anterior à data de entrada."));
        api.perform(post(caminho + "/observacoes").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"texto\": \"Sem intercorrências\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.observacoes[0]").value("Sem intercorrências"));
        api.perform(patch(caminho + "/alta").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"dataHora\": \"2000-01-01T10:00:00\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("A alta não pode ser anterior à entrada."));
        api.perform(patch(caminho + "/alta")).andExpect(status().isOk());
        api.perform(post(caminho + "/observacoes").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"texto\": \"Retorno\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void exigeReferenciasCadastradasEEstadosValidos() throws Exception {
        long disponivel = profissional("CRM 203", true);
        long indisponivel = profissional("CRM 204", false);
        long paciente = paciente("Paciente Regras", "119.704.000-04");
        long quarto = quarto("904", 1);

        api.perform(post("/internacoes").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Preencha o campo Paciente."));
        internar(9999, disponivel, quarto, "")
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Paciente 9999 não encontrado."));
        internar(paciente, 9999, quarto, "")
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Profissional 9999 não encontrado."));
        internar(paciente, disponivel, 9999, "")
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Quarto não encontrado."));
        internar(paciente, indisponivel, quarto, "")
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("O profissional Paula Mendes está indisponível."));
        internar(paciente, disponivel, quarto, ", \"dataPrevistaAlta\": \"2000-01-01\"")
                .andExpect(status().isBadRequest());
        api.perform(get("/quartos/" + quarto))
                .andExpect(jsonPath("$.ocupados").value(0));

        api.perform(patch("/pacientes/" + paciente + "/situacao").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ativo\": false}"))
                .andExpect(status().isOk());
        internar(paciente, disponivel, quarto, "")
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Paciente inativo não pode ser internado."));
        api.perform(get("/internacoes/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Internação 9999 não encontrada."));
    }

    private ResultActions internar(long paciente, long profissional, long quarto, String outrosCampos)
            throws Exception {
        String corpo = """
                {"pacienteId": %d, "profissionalId": %d, "quartoId": %d%s}
                """.formatted(paciente, profissional, quarto, outrosCampos);
        return api.perform(post("/internacoes").contentType(MediaType.APPLICATION_JSON).content(corpo));
    }

    private long paciente(String nome, String cpf) throws Exception {
        return criar("/pacientes", "{\"nome\": \"%s\", \"cpf\": \"%s\"}".formatted(nome, cpf));
    }

    private long profissional(String registro, boolean disponivel) throws Exception {
        return criar("/profissionais", """
                {"categoria": "CLINICO_GERAL", "nome": "Paula Mendes", "registro": "%s",
                 "telefone": "555-0100", "email": "paula@example.com", "disponivel": %b}
                """.formatted(registro, disponivel));
    }

    private long quarto(String numero, int capacidade) throws Exception {
        return criar("/quartos", """
                {"numero": "%s", "andar": "9", "tipo": "ENFERMARIA", "capacidade": %d}
                """.formatted(numero, capacidade));
    }

    private long criar(String caminho, String corpo) throws Exception {
        String location = api.perform(post(caminho).contentType(MediaType.APPLICATION_JSON).content(corpo))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");
        return Long.parseLong(location.substring(location.lastIndexOf('/') + 1));
    }
}
