import FormCard from '../components/FormCard/FormCard';
import PageHeader from '../components/PageHeader/PageHeader';
import RulesCard from '../components/RulesCard/RulesCard';

export default function NovoQuarto() {
  return (
    <section>
      <PageHeader
        eyebrow="Cadastros / Estrutura"
        title="Adicionar quarto"
        description="Configure capacidade para receber internações."
        action={{ href: "/quartos", label: "← Voltar para quartos", variant: "ghost" }}
      />
      <FormCard description="Quartos ocupados não podem exceder sua capacidade." submitLabel="Salvar quarto">
        <label>
          Número do quarto
          <input required placeholder="Ex.: 101" />
        </label>
        <label>
          Andar
          <input placeholder="Ex.: 1º andar" />
        </label>
        <label>
          Tipo
          <select>
            <option>Enfermaria</option>
            <option>Isolamento</option>
            <option>UTI</option>
            <option>Suíte</option>
          </select>
        </label>
        <label>
          Quantidade de leitos
          <input type="number" min="1" max="8" defaultValue="2" />
        </label>
      </FormCard>
      <RulesCard
        title="Regras desta tela"
        items={[
          "Número do quarto não pode se repetir.",
          "A capacidade precisa ser maior que zero.",
          "Uma internação só usa quarto disponível.",
        ]}
      />
    </section>
  );
}
