import { historicoMariana } from '../data/historico';
import Avatar from '../components/Avatar/Avatar';
import Button from '../components/Button/Button';
import CardHeader from '../components/CardHeader/CardHeader';
import PageHeader from '../components/PageHeader/PageHeader';
import PatientCard from '../components/PatientCard/PatientCard';
import RulesCard from '../components/RulesCard/RulesCard';
import StatusBadge from '../components/StatusBadge/StatusBadge';
import Timeline from '../components/Timeline/Timeline';
import styles from '../styles/PageContent.module.css';

export default function FichaMariana() {
  return (
    <section>
      <PageHeader
        eyebrow="Pacientes / Ficha clínica"
        title="Mariana Alves"
        description="Cadastro completo, vínculos e histórico médico."
        action={{ href: "/pacientes", label: "← Voltar para pacientes", variant: "ghost" }}
      />
      <div className={styles.detailGrid}>
        <PatientCard>
          <Avatar tone="violet" size="large">MA</Avatar>
          <h2>Mariana Alves</h2>
          <p>CPF •••.482.190-••</p>
          <hr />
          <dl>
            <dt>Data de nascimento</dt>
            <dd>12 de agosto de 1989</dd>
            <dt>Telefone</dt>
            <dd>(11) 98888-2211</dd>
            <dt>Status</dt>
            <dd>
              <StatusBadge tone="ok">Paciente ativo</StatusBadge>
            </dd>
          </dl>
          <Button href="/nova-consulta" variant="secondary">＋ Nova consulta</Button>
        </PatientCard>
        <article className="surface">
          <CardHeader
            title="Histórico de atendimentos"
            description="Eventos vinculados a esta paciente"
          />
          <Timeline events={historicoMariana} />
        </article>
      </div>
      <RulesCard
        title="Relacionamentos exibidos"
        description="Paciente 1:N atendimentos. Cada consulta possui 1 profissional responsável. O histórico preserva consultas e internações."
      />
    </section>
  );
}
