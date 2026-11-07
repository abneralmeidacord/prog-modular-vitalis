package br.com.vitalis.profissionais;

import java.time.LocalDateTime;

public class Cirurgiao extends Profissional implements AtuacaoCirurgica {
    private final String areaCirurgica;

    public Cirurgiao(long id, String nome, String registro, String telefone, String email, boolean disponivel,
                     String areaCirurgica) {
        super(id, nome, registro, telefone, email, disponivel);
        this.areaCirurgica = Validacao.textoObrigatorio(areaCirurgica, "Área cirúrgica");
    }

    @Override
    public Categoria getCategoria() {
        return Categoria.CIRURGIAO;
    }

    @Override
    public String getArea() {
        return areaCirurgica;
    }

    public String getAreaCirurgica() {
        return areaCirurgica;
    }

    @Override
    public VinculoCirurgico vincularProcedimento(long pacienteId, LocalDateTime dataHora, String descricao) {
        return new VinculoCirurgico(this, pacienteId, dataHora, descricao);
    }
}
