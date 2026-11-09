package br.com.vitalis.internacoes;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.vitalis.atendimentos.Atendimento;
import br.com.vitalis.geralExceptions.ConflitoException;
import br.com.vitalis.geralExceptions.DadosInvalidosException;
import br.com.vitalis.geralExceptions.Validacao;
import br.com.vitalis.pacientes.Paciente;
import br.com.vitalis.profissionais.Profissional;
import br.com.vitalis.quartos.Quarto;

public class Internacao extends Atendimento {
    public enum Status {
        ATIVA, ENCERRADA
    }

    private final Quarto quarto;
    private final LocalDateTime dataEntrada;
    private volatile LocalDate dataPrevistaAlta;
    private volatile LocalDateTime dataEfetivaAlta;

    private Internacao(long id, Paciente paciente, Profissional profissional, Quarto quarto,
                       LocalDateTime dataEntrada, LocalDate dataPrevistaAlta, String observacao) {
        super(id, paciente, profissional, observacao);
        this.quarto = quarto;
        this.dataEntrada = Validacao.obrigatorio(dataEntrada, "Data de entrada");
        if (dataPrevistaAlta != null) {
            validarPrevisao(dataPrevistaAlta);
        }
        this.dataPrevistaAlta = dataPrevistaAlta;
    }

    /**
     * Interna o paciente: confere os estados de paciente, profissional e quarto, ocupa um leito
     * e registra o atendimento no histórico do paciente. Nada é alterado se alguma regra falhar.
     */
    public static Internacao internar(long id, Paciente paciente, Profissional profissional, Quarto quarto,
                                      LocalDateTime dataEntrada, LocalDate dataPrevistaAlta, String observacao) {
        Validacao.obrigatorio(paciente, "Paciente");
        Validacao.obrigatorio(profissional, "Profissional responsável");
        Validacao.obrigatorio(quarto, "Quarto");
        synchronized (paciente) {
            synchronized (quarto) {
                if (!paciente.isAtivo()) {
                    throw new ConflitoException("Paciente inativo não pode ser internado.");
                }
                if (paciente.isInternado()) {
                    throw new ConflitoException("O paciente " + paciente.getNome() + " já possui uma internação ativa.");
                }
                if (!profissional.isDisponivel()) {
                    throw new ConflitoException("O profissional " + profissional.getNome() + " está indisponível.");
                }
                if (!quarto.isDisponivel()) {
                    throw new ConflitoException("O quarto " + quarto.getNumero() + " não tem leito disponível.");
                }
                Internacao internacao = new Internacao(id, paciente, profissional, quarto, dataEntrada,
                        dataPrevistaAlta, observacao);
                quarto.ocupar();
                paciente.getHistoricoMedico().adicionarAtendimento(internacao);
                return internacao;
            }
        }
    }

    public Quarto getQuarto() {
        return quarto;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public LocalDate getDataPrevistaAlta() {
        return dataPrevistaAlta;
    }

    public LocalDateTime getDataEfetivaAlta() {
        return dataEfetivaAlta;
    }

    public Status getStatus() {
        return estaAtiva() ? Status.ATIVA : Status.ENCERRADA;
    }

    public boolean estaAtiva() {
        return dataEfetivaAlta == null;
    }

    public boolean isAtiva() {
        return estaAtiva();
    }

    @JsonIgnore
    @Override
    public LocalDateTime getDataHoraInicio() {
        return dataEntrada;
    }

    public synchronized void preverAlta(LocalDate data) {
        exigirAtiva("alterar a previsão de alta");
        validarPrevisao(Validacao.obrigatorio(data, "Previsão de alta"));
        this.dataPrevistaAlta = data;
    }

    /** Encerra a internação e libera o leito no quarto. */
    public synchronized void registrarAlta(LocalDateTime dataHora) {
        exigirAtiva("registrar outra alta");
        Validacao.obrigatorio(dataHora, "Data da alta");
        if (dataHora.isBefore(dataEntrada)) {
            throw new DadosInvalidosException("A alta não pode ser anterior à entrada.");
        }
        if (dataHora.isAfter(LocalDateTime.now())) {
            throw new DadosInvalidosException("A alta não pode ser registrada em data futura.");
        }
        quarto.liberar();
        this.dataEfetivaAlta = dataHora;
    }

    @Override
    public synchronized void registrarObservacao(String texto) {
        exigirAtiva("registrar observações");
        super.registrarObservacao(texto);
    }

    private void validarPrevisao(LocalDate data) {
        if (data.isBefore(dataEntrada.toLocalDate())) {
            throw new DadosInvalidosException("A previsão de alta não pode ser anterior à data de entrada.");
        }
    }

    private void exigirAtiva(String acao) {
        if (!estaAtiva()) {
            throw new ConflitoException("A internação " + getId() + " já foi encerrada; não é possível " + acao + ".");
        }
    }
}
