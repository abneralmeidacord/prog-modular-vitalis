package br.com.vitalis.quartos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

@Service
public class QuartoService {

    public record Cadastro(
        String numero,
        String andar,
        TipoQuarto tipo,
        int capacidade
    ) {}

    private final Map<Long, Quarto> quartos =
        new ConcurrentHashMap<>();

    private final AtomicLong sequencia = new AtomicLong();

    public synchronized Quarto cadastrar(Cadastro dados) {
        if (dados == null) {
            throw new IllegalArgumentException(
                "Informe os dados do quarto.");
        }

        boolean duplicado = quartos.values().stream()
            .anyMatch(q -> q.getNumero()
                .equalsIgnoreCase(
                    dados.numero() == null
                        ? ""
                        : dados.numero().trim()
                ));

        if (duplicado) {
            throw new IllegalArgumentException(
                "Já existe um quarto com esse número.");
        }

        Quarto quarto = new Quarto(
            sequencia.incrementAndGet(),
            dados.numero(),
            dados.andar(),
            dados.tipo(),
            dados.capacidade()
        );

        quartos.put(quarto.getId(), quarto);
        return quarto;
    }

    public List<Quarto> listar() {
        List<Quarto> resultado =
            new ArrayList<>(quartos.values());

        resultado.sort(
            Comparator.comparing(Quarto::getId)
        );

        return resultado;
    }

    public Quarto buscar(Long id) {
        Quarto quarto = quartos.get(id);

        if (quarto == null) {
            throw new QuartoNaoEncontradoException(
                "Quarto não encontrado.");
        }

        return quarto;
    }

    public List<Quarto> listarDisponiveis() {
        return listar().stream()
            .filter(Quarto::isDisponivel)
            .toList();
    }

    public synchronized void ocupar(Long id) {
        buscar(id).ocupar();
    }

    public synchronized void liberar(Long id) {
        buscar(id).liberar();
    }
}
