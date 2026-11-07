import FormCard from '../components/FormCard/FormCard';
import PageHeader from '../components/PageHeader/PageHeader';
import RulesCard from '../components/RulesCard/RulesCard';

export default function NovaInternacao() {
  return (
    <section>
      <PageHeader
        eyebrow="Atendimentos / Internações"
        title="Registrar internação"
        description="Associe paciente, profissional responsável e leito disponível."
        action={{ href: "/internacoes", label: "← Voltar para internações", variant: "ghost" }}
      />
      <FormCard description="Somente quartos com leito disponível poderão ser selecionados." submitLabel="Confirmar internação">
        <label>
          Paciente
          <select required defaultValue="">
            <option value="" disabled>Selecione o paciente</option>
            <option>Rafael Costa</option>
            <option>Lucas Soares</option>
          </select>
        </label>
        <label>
          Profissional responsável
          <select required defaultValue="">
            <option value="" disabled>Selecione o profissional</option>
            <option>Dra. Paula Mendes</option>
            <option>Dr. André Freitas</option>
          </select>
        </label>
        <label>
          Quarto disponível
          <select required>
            <option>101 · Enfermaria</option>
            <option>305 · Isolamento</option>
          </select>
        </label>
        <label>
          Previsão de alta
          <input type="date" />
        </label>
        <label data-wide="true">
          Motivo da internação
          <textarea placeholder="Descrição inicial e observações" />
        </label>
      </FormCard>
      <RulesCard
        title="Regras desta tela"
        items={[
          "Paciente e profissional são obrigatórios.",
          "O quarto precisa estar disponível e dentro da capacidade.",
          "A alta libera a vaga e preserva o histórico.",
        ]}
      />
    </section>
  );
}
