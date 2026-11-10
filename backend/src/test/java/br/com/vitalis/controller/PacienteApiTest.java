package br.com.vitalis.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
class PacienteApiTest {
    @Autowired
    private MockMvc api;

    @Test
    void cadastraPacienteAtivoSemInternacaoEBuscaPeloId() throws Exception {
        String caminho = cadastrar("Mariana Alves", "48219012345", ", \"dataNascimento\": \"1989-08-12\"")
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.cpf").value("482.190.123-45"))
                .andExpect(jsonPath("$.ativo").value(true))
                .andExpect(jsonPath("$.internado").value(false))
                .andExpect(jsonPath("$.idade").isNumber())
                .andExpect(jsonPath("$.historicoMedico").doesNotExist())
                .andReturn().getResponse().getHeader("Location");

        api.perform(get(caminho))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Mariana Alves"));
        api.perform(get(caminho + "/internacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void buscaPorNomeOuCpf() throws Exception {
        cadastrar("Aline Ribeiro Busca", "832.551.000-11", "").andExpect(status().isCreated());

        api.perform(get("/pacientes").param("busca", "  ribeiro busca "))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].cpf").value("832.551.000-11"));
        api.perform(get("/pacientes").param("busca", "832.551.000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nome").value("Aline Ribeiro Busca"));
        api.perform(get("/pacientes").param("busca", " "))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Preencha o campo Busca."));
    }

    @Test
    void rejeitaDadosInvalidosCpfDuplicadoEIdDesconhecido() throws Exception {
        cadastrar(" ", "100.200.300-40", "")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Preencha o campo Nome."));
        cadastrar("Lucas Soares", "123", "")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("O CPF deve ter 11 dígitos."));
        cadastrar("Lucas Soares", "100.200.300-40", "").andExpect(status().isCreated());
        cadastrar("Outro Nome", "10020030040", "")
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Já existe um paciente com o CPF 100.200.300-40."));
        api.perform(get("/pacientes/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Paciente 9999 não encontrado."));
    }

    @Test
    void atualizaContatoESituacao() throws Exception {
        String caminho = cadastrar("Rafael Contato", "555.666.777-88", "")
                .andReturn().getResponse().getHeader("Location");

        api.perform(put(caminho + "/contato").contentType(MediaType.APPLICATION_JSON).content("""
                        {"telefone": "(31) 98888-0000", "email": "rafael@example.com", "endereco": "Rua A, 10"}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.telefone").value("(31) 98888-0000"))
                .andExpect(jsonPath("$.endereco").value("Rua A, 10"));
        api.perform(put(caminho + "/contato").contentType(MediaType.APPLICATION_JSON).content("""
                        {"email": "invalido"}
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("E-mail inválido."));
        api.perform(patch(caminho + "/situacao").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Preencha o campo Ativo."));
        api.perform(patch(caminho + "/situacao").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ativo\": false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ativo").value(false))
                .andExpect(jsonPath("$.telefone").value("(31) 98888-0000"));
    }

    private ResultActions cadastrar(String nome, String cpf, String outrosCampos) throws Exception {
        String corpo = """
                {"nome": "%s", "cpf": "%s"%s}
                """.formatted(nome, cpf, outrosCampos);
        return api.perform(post("/pacientes").contentType(MediaType.APPLICATION_JSON).content(corpo));
    }
}
