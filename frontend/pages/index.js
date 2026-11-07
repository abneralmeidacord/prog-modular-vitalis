import Agenda from '../components/Agenda/Agenda';
import DashboardStats from '../components/DashboardStats/DashboardStats';
import PageHeader from '../components/PageHeader/PageHeader';
import QuickActions from '../components/QuickActions/QuickActions';
import RulesCard from '../components/RulesCard/RulesCard';
import styles from '../styles/PageContent.module.css';

export default function Dashboard() {
  return (
    <section>
      <PageHeader
        eyebrow="Terça-feira, 14 de maio de 2024"
        title="Bom dia, Administração"
        description="Acompanhe a operação do hospital em um só lugar."
        action={{ href: "/nova-consulta", label: "＋ Novo atendimento" }}
        hero
      />
      <DashboardStats />
      <section className={styles.twoCol}>
        <Agenda />
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
