package br.com.vitalis.model;

import br.com.vitalis.entity.Profissional;

public record CadastroProfissional(Profissional.Categoria categoria, String nome, String registro, String telefone,
        String email, Boolean disponivel, String especialidade, String areaCirurgica) {
}
