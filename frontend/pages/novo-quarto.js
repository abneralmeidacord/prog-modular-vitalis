import { useState } from 'react';
import { tiposQuarto } from '../lib/format';
import FormCard from '../components/FormCard/FormCard';
import PageHeader from '../components/PageHeader/PageHeader';
import RulesCard from '../components/RulesCard/RulesCard';

export default function NovoQuarto() {
  const [tipo, setTipo] = useState('ENFERMARIA');

  return (
    <section>
      <PageHeader
        eyebrow="Cadastros / Estrutura"
        title="Adicionar quarto"
        description="Configure capacidade para receber internações."
        action={{ href: "/quartos", label: "← Voltar para quartos", variant: "ghost" }}
      />
      <FormCard endpoint="/quartos" destination="/quartos"
        toPayload={fields => ({ ...fields, capacidade: Number(fields.capacidade) })}
        description="Quartos ocupados não podem exceder sua capacidade." submitLabel="Salvar quarto">
        <label>
          Número do quarto
          <input name="numero" required placeholder="Ex.: 101" />
        </label>
        <label>
          Andar
          <input name="andar" placeholder="Ex.: 1º andar" />
        </label>
        <label>
          Tipo
          <select name="tipo" value={tipo} onChange={event => setTipo(event.target.value)}>
            {Object.entries(tiposQuarto).map(([value, quarto]) => (
              <option key={value} value={value}>{quarto.label}</option>
            ))}
          </select>
        </label>
        <label>
          Quantidade de leitos
          <input name="capacidade" type="number" min="1" max={tiposQuarto[tipo].capacidade} defaultValue="1" required />
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
