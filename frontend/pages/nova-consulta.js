import FormCard from '../components/FormCard/FormCard';
import PageHeader from '../components/PageHeader/PageHeader';
import RulesCard from '../components/RulesCard/RulesCard';

export default function NovaConsulta() {
  return (
    <section>
      <PageHeader
        eyebrow="Atendimentos / Consultas"
        title="Agendar consulta"
        description="Vincule paciente, profissional e um horário sem conflitos."
        action={{ href: "/consultas", label: "← Voltar para agenda", variant: "ghost" }}
      />
      <FormCard description="O sistema valida a existência do paciente e conflitos de agenda." submitLabel="Confirmar agendamento">
        <label>
          Paciente
          <select required defaultValue="">
            <option value="" disabled>Selecione o paciente</option>
            <option>Mariana Alves</option>
            <option>Rafael Costa</option>
          </select>
        </label>
        <label>
          Profissional
          <select required defaultValue="">
            <option value="" disabled>Selecione o profissional</option>
            <option>Dra. Helena Martins · Cardiologia</option>
            <option>Dra. Paula Mendes · Clínica geral</option>
          </select>
        </label>
        <label>
          Data
          <input type="date" required defaultValue="2024-05-14" />
        </label>
        <label>
          Horário
          <input type="time" required defaultValue="15:30" />
        </label>
        <label data-wide="true">
          Observação
          <textarea placeholder="Motivo ou orientações do atendimento" />
        </label>
      </FormCard>
      <RulesCard
        title="Regras desta tela"
        items={[
          "Paciente e profissional são obrigatórios.",
          "O profissional não pode ter conflito de horário.",
          "O paciente pode ter múltiplas consultas.",
        ]}
      />
    </section>
  );
}
