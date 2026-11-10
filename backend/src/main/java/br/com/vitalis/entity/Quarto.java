package br.com.vitalis.entity;

import br.com.vitalis.model.TipoQuarto;

public class Quarto {

    private final Long id;
    private final String numero;
    private final String andar;
    private final TipoQuarto tipo;
    private final int capacidade;
    private int ocupados;

    public Quarto(
            Long id,
            String numero,
            String andar,
            TipoQuarto tipo,
            int capacidade) {

        if (numero == null || numero.isBlank()) {
            throw new IllegalArgumentException(
                "O número do quarto é obrigatório.");
        }

        if (tipo == null) {
            throw new IllegalArgumentException(
                "O tipo do quarto é obrigatório.");
        }

        if (capacidade < 1 ||
            capacidade > tipo.getCapacidadeMaxima()) {
            throw new IllegalArgumentException(
                "Capacidade inválida para o tipo " + tipo);
        }

        this.id = id;
        this.numero = numero.trim();
        this.andar = andar == null ? "" : andar.trim();
        this.tipo = tipo;
        this.capacidade = capacidade;
        this.ocupados = 0;
    }

    public synchronized void ocupar() {
        if (!isDisponivel()) {
            throw new IllegalStateException(
                "Quarto sem leitos disponíveis.");
        }
        ocupados++;
    }

    public synchronized void liberar() {
        if (ocupados == 0) {
            throw new IllegalStateException(
                "O quarto não possui ocupantes.");
        }
        ocupados--;
    }

    public Long getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    public String getAndar() {
        return andar;
    }

    public TipoQuarto getTipo() {
        return tipo;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public synchronized int getOcupados() {
        return ocupados;
    }

    public synchronized int getDisponiveis() {
        return capacidade - ocupados;
    }

    public synchronized boolean isDisponivel() {
        return ocupados < capacidade;
    }
}
