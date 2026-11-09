package br.com.vitalis.pacientes;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Service;

import br.com.vitalis.geralExceptions.ConflitoException;
import br.com.vitalis.geralExceptions.RecursoNaoEncontradoException;
import br.com.vitalis.geralExceptions.Validacao;
import br.com.vitalis.internacoes.Internacao;

@Service
public class PacienteService {
    private final Map<Long, Paciente> pacientes = new LinkedHashMap<>();
    private long proximoId = 1;

    public synchronized Paciente cadastrar(Cadastro cadastro) {
        Paciente paciente = new Paciente(proximoId, cadastro.nome(), cadastro.cpf(), cadastro.dataNascimento(),
                cadastro.telefone(), cadastro.email(), cadastro.endereco(), cadastro.observacoes());
        boolean cpfJaCadastrado = pacientes.values().stream()
                .anyMatch(cadastrado -> cadastrado.getCpf().equals(paciente.getCpf()));
        if (cpfJaCadastrado) {
            throw new ConflitoException("Já existe um paciente com o CPF " + paciente.getCpf() + ".");
        }
        pacientes.put(proximoId++, paciente);
        return paciente;
    }

    public synchronized List<Paciente> listar(String busca) {
        if (busca == null) {
            return List.copyOf(pacientes.values());
        }
        String termo = Validacao.textoObrigatorio(busca, "Busca").toLowerCase(Locale.ROOT);
        boolean buscaPorCpf = termo.matches("[\\d.\\-\\s]+");
        String digitos = termo.replaceAll("\\D", "");
        return pacientes.values().stream()
                .filter(paciente -> buscaPorCpf
                        ? paciente.getCpf().replaceAll("\\D", "").contains(digitos)
                        : paciente.getNome().toLowerCase(Locale.ROOT).contains(termo))
                .toList();
    }

    public synchronized Paciente buscar(long id) {
        Paciente paciente = pacientes.get(id);
        if (paciente == null) {
            throw new RecursoNaoEncontradoException("Paciente " + id + " não encontrado.");
        }
        return paciente;
    }

    public synchronized Paciente atualizarContato(long id, Contato contato) {
        Paciente paciente = buscar(id);
        paciente.atualizarContato(contato.telefone(), contato.email());
        paciente.atualizarEndereco(contato.endereco());
        return paciente;
    }

    public synchronized Paciente definirSituacao(long id, Situacao situacao) {
        Paciente paciente = buscar(id);
        paciente.definirAtivo(Validacao.obrigatorio(situacao.ativo(), "Ativo"));
        return paciente;
    }

    public synchronized List<Internacao> listarInternacoes(long id) {
        return buscar(id).getHistoricoMedico().getInternacoesRealizadas();
    }

    public record Cadastro(String nome, String cpf, LocalDate dataNascimento, String telefone, String email,
            String endereco, String observacoes) {
    }

    public record Contato(String telefone, String email, String endereco) {
    }

    public record Situacao(Boolean ativo) {
    }
}
