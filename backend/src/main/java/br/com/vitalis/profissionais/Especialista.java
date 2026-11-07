package br.com.vitalis.profissionais;

public class Especialista extends Profissional {
    private final String especialidade;

    public Especialista(long id, String nome, String registro, String telefone, String email, boolean disponivel,
                        String especialidade) {
        super(id, nome, registro, telefone, email, disponivel);
        this.especialidade = Validacao.textoObrigatorio(especialidade, "Especialidade");
    }

    @Override
    public Categoria getCategoria() {
        return Categoria.ESPECIALISTA;
    }

    @Override
    public String getArea() {
        return especialidade;
    }

    public String getEspecialidade() {
        return especialidade;
    }
}
