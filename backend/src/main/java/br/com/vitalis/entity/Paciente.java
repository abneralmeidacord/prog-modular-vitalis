package br.com.vitalis.entity;

import java.time.LocalDate;
import java.time.Period;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.vitalis.exceptions.ConflitoException;
import br.com.vitalis.exceptions.DadosInvalidosException;
import br.com.vitalis.model.Validacao;

public final class Paciente {
    private final long id;
    private final String nome;
    private final String cpf;
    private final LocalDate dataNascimento;
    private final String observacoes;
    private volatile String telefone;
    private volatile String email;
    private volatile String endereco;
    private volatile boolean ativo = true;
    private final HistoricoMedico historicoMedico;

    public Paciente(long id, String nome, String cpf, LocalDate dataNascimento, String telefone, String email,
                    String endereco, String observacoes) {
        this.id = id;
        this.nome = Validacao.textoObrigatorio(nome, "Nome");
        this.cpf = normalizarCpf(cpf);
        if (dataNascimento != null && dataNascimento.isAfter(LocalDate.now())) {
            throw new DadosInvalidosException("A data de nascimento não pode ser futura.");
        }
        this.dataNascimento = dataNascimento;
        this.telefone = Validacao.textoOpcional(telefone);
        this.email = Validacao.emailOpcional(email);
        this.endereco = Validacao.textoOpcional(endereco);
        this.observacoes = Validacao.textoOpcional(observacoes);
        this.historicoMedico = new HistoricoMedico(this);
    }

    static String normalizarCpf(String cpf) {
        String valor = Validacao.textoObrigatorio(cpf, "CPF");
        String digitos = valor.replaceAll("\\D", "");
        if (!valor.matches("[\\d.\\-\\s]+") || digitos.length() != 11) {
            throw new DadosInvalidosException("O CPF deve ter 11 dígitos.");
        }
        return digitos.substring(0, 3) + "." + digitos.substring(3, 6) + "." + digitos.substring(6, 9) + "-"
                + digitos.substring(9);
    }

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public boolean isInternado() {
        return historicoMedico.getInternacaoAtiva().isPresent();
    }

    @JsonIgnore
    public HistoricoMedico getHistoricoMedico() {
        return historicoMedico;
    }

    @JsonProperty("idade")
    public Integer calcularIdade() {
        return dataNascimento == null ? null : Period.between(dataNascimento, LocalDate.now()).getYears();
    }

    public void atualizarContato(String telefone, String email) {
        String emailValidado = Validacao.emailOpcional(email);
        this.telefone = Validacao.textoOpcional(telefone);
        this.email = emailValidado;
    }

    public void atualizarEndereco(String endereco) {
        this.endereco = Validacao.textoOpcional(endereco);
    }

    public synchronized void definirAtivo(boolean ativo) {
        if (!ativo && isInternado()) {
            throw new ConflitoException("Paciente internado não pode ser inativado. Registre a alta antes.");
        }
        this.ativo = ativo;
    }
}
