package br.com.vitalis.profissionais;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class ProfissionalService {
    private final Map<Long, Profissional> profissionais = new LinkedHashMap<>();
    private long proximoId = 1;

    public synchronized Profissional cadastrar(Cadastro cadastro) {
        if (cadastro.categoria() == null) {
            throw new DadosInvalidosException("Categoria é obrigatória.");
        }
        boolean disponivel = !Boolean.FALSE.equals(cadastro.disponivel());
        Profissional profissional = switch (cadastro.categoria()) {
            case CLINICO_GERAL -> new ClinicoGeral(proximoId, cadastro.nome(), cadastro.registro(),
                    cadastro.telefone(), cadastro.email(), disponivel);
            case ESPECIALISTA -> new Especialista(proximoId, cadastro.nome(), cadastro.registro(),
                    cadastro.telefone(), cadastro.email(), disponivel, cadastro.especialidade());
            case CIRURGIAO -> new Cirurgiao(proximoId, cadastro.nome(), cadastro.registro(),
                    cadastro.telefone(), cadastro.email(), disponivel, cadastro.areaCirurgica());
        };
        boolean registroJaCadastrado = profissionais.values().stream()
                .anyMatch(cadastrado -> cadastrado.getRegistro().equalsIgnoreCase(profissional.getRegistro()));
        if (registroJaCadastrado) {
            throw new RegistroDuplicadoException(profissional.getRegistro());
        }
        profissionais.put(proximoId++, profissional);
        return profissional;
    }

    public synchronized List<Profissional> listar(String area) {
        if (area == null) {
            return List.copyOf(profissionais.values());
        }
        String areaBuscada = Validacao.textoObrigatorio(area, "Área");
        return profissionais.values().stream()
                .filter(Profissional::isDisponivel)
                .filter(profissional -> profissional.getArea().equalsIgnoreCase(areaBuscada))
                .toList();
    }

    public synchronized Profissional buscar(long id) {
        Profissional profissional = profissionais.get(id);
        if (profissional == null) {
            throw new ProfissionalNaoEncontradoException(id);
        }
        return profissional;
    }

    public record Cadastro(Profissional.Categoria categoria, String nome, String registro, String telefone,
            String email, Boolean disponivel, String especialidade, String areaCirurgica) {
    }
}
