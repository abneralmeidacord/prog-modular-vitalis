import FormCard from '../components/FormCard/FormCard';
import PageHeader from '../components/PageHeader/PageHeader';
import RulesCard from '../components/RulesCard/RulesCard';

export default function NovoProfissional() {
  return (
    <section>
      <PageHeader
        eyebrow="Cadastros / Profissionais"
        title="Novo profissional"
        description="Registre a equipe que poderá assumir atendimentos."
        action={{ href: "/profissionais", label: "← Voltar para profissionais", variant: "ghost" }}
      />
      <FormCard description="O profissional ficará disponível para vínculo em consultas." submitLabel="Salvar profissional">
        <label>
          Nome completo
          <input required placeholder="Ex.: Dra. Helena Martins" />
        </label>
        <label>
          CRM / registro
          <input required placeholder="Ex.: CRM 48291" />
        </label>
        <label>
          Especialidade
          <select defaultValue="">
            <option value="">Selecione</option>
            <option>Cardiologia</option>
            <option>Clínica geral</option>
            <option>Ortopedia</option>
            <option>Neurologia</option>
          </select>
        </label>
        <label>
          E-mail profissional
          <input type="email" placeholder="profissional@hospital.com" />
        </label>
      </FormCard>
      <RulesCard
        title="Regras desta tela"
        items={[
          "Nome, registro e especialidade são obrigatórios.",
          "Um profissional não pode ter dois atendimentos no mesmo horário.",
        ]}
      />
    </section>
  );
}
