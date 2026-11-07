import { historicoMariana } from '../data/historico';
import Avatar from '../components/Avatar/Avatar';
import Button from '../components/Button/Button';
import PageHeader from '../components/PageHeader/PageHeader';
import PatientCard from '../components/PatientCard/PatientCard';
import Timeline from '../components/Timeline/Timeline';
import styles from '../styles/PageContent.module.css';

export default function Historico() {
  return (
    <section>
      <PageHeader
        eyebrow="Prontuários"
        title="Histórico médico"
        description="Uma visão cronológica dos atendimentos e observações relevantes."
      />
      <div className={`${styles.history} surface`}>
        <PatientCard as="div">
          <Avatar tone="violet" size="large">MA</Avatar>
          <h2>Mariana Alves</h2>
          <p>CPF •••.482.190-•• · 34 anos</p>
          <Button href="/ficha-mariana" variant="secondary">Abrir ficha completa</Button>
        </PatientCard>
        <Timeline events={historicoMariana} />
      </div>
    </section>
  );
}
