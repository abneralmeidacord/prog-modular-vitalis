import RequestStatus from '../components/RequestStatus/RequestStatus';
import { useApi } from '../lib/api';
import Agenda from '../components/Agenda/Agenda';
import DashboardStats from '../components/DashboardStats/DashboardStats';
import PageHeader from '../components/PageHeader/PageHeader';
import QuickActions from '../components/QuickActions/QuickActions';
import RulesCard from '../components/RulesCard/RulesCard';
import styles from '../styles/PageContent.module.css';

export default function Dashboard() {
  const pacientes = useApi('/pacientes');
  const consultas = useApi('/consultas');
  const internacoes = useApi('/internacoes?ativas=true');
  const quartos = useApi('/quartos');
  const requests = [pacientes, consultas, internacoes, quartos];
  const request = { loading: requests.some(item => item.loading), error: requests.find(item => item.error)?.error };
  const hoje = new Intl.DateTimeFormat('sv-SE').format(new Date());
  const agenda = (consultas.data || []).filter(({ consulta }) => consulta.data === hoje)
    .sort((a, b) => a.consulta.horario.localeCompare(b.consulta.horario));
  const carregado = requests.every(item => item.data);

  return (
    <section>
      <PageHeader
        eyebrow="Visão geral"
        title="Bom dia, Administração"
        description="Acompanhe a operação do hospital em um só lugar."
        action={{ href: "/nova-consulta", label: "＋ Novo atendimento" }}
        hero
      />
      <RequestStatus request={request} />
      {carregado && <DashboardStats pacientes={pacientes.data} consultas={agenda}
        internacoes={internacoes.data} quartos={quartos.data} />}
      <section className={styles.twoCol}>
        {pacientes.data && consultas.data && <Agenda pacientes={pacientes.data} consultas={agenda} />}
        <RulesCard
          title="Regras de negócio"
          compact
          description="Integridade aplicada nesta tela"
          items={[
            "Consulta exige paciente e profissional.",
            "Profissional não pode ter conflito de horário.",
            "Paciente pode ter vários atendimentos.",
          ]}
        />
      </section>
      <QuickActions />
    </section>
  );
}
