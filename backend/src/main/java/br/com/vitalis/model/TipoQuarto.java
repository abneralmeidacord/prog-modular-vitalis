package br.com.vitalis.model;

public enum TipoQuarto {
    ENFERMARIA(8),
    ISOLAMENTO(1),
    UTI(1),
    SUITE(2);

    private final int capacidadeMaxima;

    TipoQuarto(int capacidadeMaxima) {
        this.capacidadeMaxima = capacidadeMaxima;
    }

    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }
}
