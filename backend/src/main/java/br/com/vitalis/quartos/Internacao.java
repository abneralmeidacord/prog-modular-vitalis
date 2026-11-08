package br.com.vitalis.quartos;

import java.time.LocalDate;

public class Internacao {

    public enum Status {
        ATIVA, ENCERRADA
    }

    private final Long id;
    private final Long pacienteId;
    private final Long profissionalId;
    private final Long quartoId;
    private final LocalDate dataEntrada;
    private LocalDate dataAlta;
    private Status status;

    public Internacao(
            Long id,
            Long pacienteId,
            Long profissionalId,
            Long quartoId) {

        if (pacienteId == null || pacienteId <= 0 ||
            profissionalId == null || profissionalId <= 0 ||
            quartoId == null || quartoId <= 0) {
            throw new IllegalArgumentException(
                "Paciente, profissional e quarto são obrigatórios.");
        }

        this.id = id;
        this.pacienteId = pacienteId;
        this.profissionalId = profissionalId;
        this.quartoId = quartoId;
        this.dataEntrada = LocalDate.now();
        this.status = Status.ATIVA;
    }

    public void encerrar() {
        if (status == Status.ENCERRADA) {
            throw new IllegalStateException(
                "Esta internação já foi encerrada.");
        }

        status = Status.ENCERRADA;
        dataAlta = LocalDate.now();
    }

    public Long getId() { return id; }
    public Long getPacienteId() { return pacienteId; }
    public Long getProfissionalId() { return profissionalId; }
    public Long getQuartoId() { return quartoId; }
    public LocalDate getDataEntrada() { return dataEntrada; }
    public LocalDate getDataAlta() { return dataAlta; }
    public Status getStatus() { return status; }
}
