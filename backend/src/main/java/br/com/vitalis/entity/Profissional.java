package br.com.vitalis.entity;

import br.com.vitalis.exceptions.DadosProfissionaisInvalidosException;
import br.com.vitalis.model.ValidacaoProfissional;

public abstract class Profissional {
    public enum Categoria {
        CLINICO_GERAL, ESPECIALISTA, CIRURGIAO
    }

    private final long id;
    private final String nome;
    private final String registro;
    private final String telefone;
    private final String email;
    private final boolean disponivel;

    protected Profissional(long id, String nome, String registro, String telefone, String email, boolean disponivel) {
        this.id = id;
        this.nome = ValidacaoProfissional.textoObrigatorio(nome, "Nome");
        this.registro = ValidacaoProfissional.textoObrigatorio(registro, "Registro profissional");
        this.telefone = ValidacaoProfissional.textoObrigatorio(telefone, "Telefone");
        this.email = ValidacaoProfissional.textoObrigatorio(email, "E-mail");
        if (!this.email.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")) {
            throw new DadosProfissionaisInvalidosException("E-mail inválido.");
        }
        this.disponivel = disponivel;
    }

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getRegistro() {
        return registro;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public abstract Categoria getCategoria();

    public abstract String getArea();
}
