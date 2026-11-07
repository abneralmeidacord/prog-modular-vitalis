import FormCard from '../components/FormCard/FormCard';
import PageHeader from '../components/PageHeader/PageHeader';
import RulesCard from '../components/RulesCard/RulesCard';

export default function NovoPaciente() {
  return (
    <section>
      <PageHeader
        eyebrow="Cadastros / Pacientes"
        title="Nova ficha de paciente"
        description="Cadastre a identidade e os contatos do paciente."
        action={{ href: "/pacientes", label: "← Voltar para pacientes", variant: "ghost" }}
      />
      <FormCard description="O paciente poderá ser vinculado a várias consultas e internações." submitLabel="Salvar paciente">
        <label>
          Nome completo
          <input required placeholder="Ex.: Mariana Alves" />
        </label>
        <label>
          CPF
          <input required placeholder="000.000.000-00" />
        </label>
        <label>
          Data de nascimento
          <input type="date" />
        </label>
        <label>
          Telefone
          <input placeholder="(00) 00000-0000" />
        </label>
        <label data-wide="true">
          Endereço
          <input placeholder="Rua, número, bairro e cidade" />
        </label>
        <label data-wide="true">
          Observações
          <textarea placeholder="Informações relevantes para o atendimento" />
        </label>
      </FormCard>
      <RulesCard
        title="Regras desta tela"
        items={[
          "Nome e CPF são obrigatórios.",
          "O CPF deve ser único no cadastro.",
          "Dados de contato podem ser atualizados depois.",
        ]}
      />
    </section>
  );
}
