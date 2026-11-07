import { historicoRafael } from '../data/historico';
import Avatar from '../components/Avatar/Avatar';
import Button from '../components/Button/Button';
import CardHeader from '../components/CardHeader/CardHeader';
import PageHeader from '../components/PageHeader/PageHeader';
import PatientCard from '../components/PatientCard/PatientCard';
import StatusBadge from '../components/StatusBadge/StatusBadge';
import Timeline from '../components/Timeline/Timeline';
import styles from '../styles/PageContent.module.css';

export default function FichaRafael() {
  return (
    <section>
      <PageHeader
        eyebrow="Pacientes / Ficha clínica"
        title="Rafael Costa"
        description="Paciente internado no quarto 204."
        action={{ href: "/pacientes", label: "← Voltar para pacientes", variant: "ghost" }}
      />
      <div className={styles.detailGrid}>
        <PatientCard>
          <Avatar size="large">RC</Avatar>
          <h2>Rafael Costa</h2>
          <p>CPF •••.119.704-••</p>
          <StatusBadge tone="info">Internado · Quarto 204</StatusBadge>
          <hr />
          <dl>
            <dt>Profissional responsável</dt>
            <dd>Dra. Paula Mendes</dd>
            <dt>Entrada</dt>
            <dd>12 de maio de 2024, 16:20</dd>
            <dt>Previsão de alta</dt>
            <dd>18 de maio de 2024</dd>
          </dl>
          <Button href="/internacoes" variant="secondary">Ver internação</Button>
        </PatientCard>
        <article className="surface">
          <CardHeader
            title="Histórico de atendimentos"
            description="Acompanhamento assistencial"
          />
          <Timeline events={historicoRafael} />
        </article>
      </div>
    </section>
  );
}
