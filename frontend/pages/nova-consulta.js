import { useRouter } from 'next/router';
import CadastroSelect from '../components/CadastroSelect/CadastroSelect';
import RequestStatus from '../components/RequestStatus/RequestStatus';
import { useApi } from '../lib/api';
import FormCard from '../components/FormCard/FormCard';
import PageHeader from '../components/PageHeader/PageHeader';
import RulesCard from '../components/RulesCard/RulesCard';

export default function NovaConsulta() {
  const { query } = useRouter();
  const pacientes = useApi('/pacientes');
  const profissionais = useApi('/profissionais');
  const pacientesAtivos = (pacientes.data || []).filter(paciente => paciente.ativo);
  const equipe = (profissionais.data || []).filter(profissional => profissional.disponivel &&
    profissional.categoria !== 'CIRURGIAO');
  const pacienteId = pacientesAtivos.find(paciente => String(paciente.id) === query.pacienteId)?.id ?? '';

  function toPayload(fields) {
    const profissional = equipe.find(item => item.id === Number(fields.profissionalId));
    const especializada = profissional.categoria === 'ESPECIALISTA';
    return {
      ...fields,
      pacienteId: Number(fields.pacienteId),
      profissionalId: profissional.id,
      tipo: especializada ? 'ESPECIALIZADA' : 'ROTINA',
      especialidade: especializada ? profissional.area : null,
    };
  }

  return (
    <section>
      <PageHeader
        eyebrow="Atendimentos / Consultas"
        title="Agendar consulta"
        description="Vincule paciente, profissional e um horário sem conflitos."
        action={{ href: "/consultas", label: "← Voltar para agenda", variant: "ghost" }}
      />
      <RequestStatus request={{ ...pacientes, data: pacientesAtivos }} empty="Cadastre um paciente ativo para agendar." />
      <RequestStatus request={{ ...profissionais, data: equipe }} empty="Cadastre um clínico ou especialista disponível." />
      <FormCard endpoint="/consultas" destination="/consultas" toPayload={toPayload}
        disabled={!pacientesAtivos.length || !equipe.length} description="O sistema valida a existência do paciente e conflitos de agenda." submitLabel="Confirmar agendamento">
        <CadastroSelect key={pacienteId} label="Paciente" name="pacienteId" options={pacientesAtivos} defaultValue={pacienteId} />
        <CadastroSelect label="Profissional" name="profissionalId" options={equipe}
          describe={profissional => profissional.nome + ' · ' + profissional.area} />
        <label>
          Data
          <input name="data" type="date" required />
        </label>
        <label>
          Horário
          <input name="horario" type="time" required />
        </label>
        <label data-wide="true">
          Motivo
          <textarea name="motivo" required placeholder="Motivo ou orientações do atendimento" />
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
