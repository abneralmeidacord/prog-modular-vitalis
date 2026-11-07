package br.com.vitalis.profissionais;

public record Encaminhamento(ClinicoGeral clinico, long pacienteId, String especialidade, String motivo) {
    public Encaminhamento {
        if (clinico == null) {
            throw new DadosInvalidosException("Clínico é obrigatório.");
        }
        Validacao.pacienteObrigatorio(pacienteId);
        especialidade = Validacao.textoObrigatorio(especialidade, "Especialidade de destino");
        motivo = Validacao.textoObrigatorio(motivo, "Motivo");
    }
}
