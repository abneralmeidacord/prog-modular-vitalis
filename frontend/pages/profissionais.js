import Avatar from '../components/Avatar/Avatar';
import PageHeader from '../components/PageHeader/PageHeader';
import RulesCard from '../components/RulesCard/RulesCard';
import styles from '../styles/PageContent.module.css';

export default function Profissionais() {
  return (
    <section>
      <PageHeader
        eyebrow="Cadastro assistencial"
        title="Profissionais"
        description="Equipe, registro profissional, especialidades e disponibilidade."
        action={{ href: "/novo-profissional", label: "＋ Novo profissional" }}
      />
      <div className={styles.entityGrid}>
        <article className={`surface ${styles.entity}`}>
          <div className={styles.sectionHead}>
            <h2>Equipe médica</h2>
            <span className={styles.badge}>42 ativos</span>
          </div>
          <div className={styles.team}>
            <p>
              <Avatar tone="violet">HM</Avatar>
              <span>
                <b>Dra. Helena Martins</b>
                <small>Cardiologia · CRM 48291</small>
              </span>
              <span className={styles.availability}>● Disponível</span>
            </p>
            <p>
              <Avatar>GN</Avatar>
              <span>
                <b>Dr. Gustavo Nunes</b>
                <small>Ortopedia · CRM 39104</small>
              </span>
              <span className={styles.availability}>● Em atendimento</span>
            </p>
            <p>
              <Avatar tone="peach">PM</Avatar>
              <span>
                <b>Dra. Paula Mendes</b>
                <small>Clínica geral · CRM 51782</small>
              </span>
              <span className={styles.availability}>● Disponível</span>
            </p>
          </div>
        </article>
        <RulesCard
          title="Regras de negócio"
          items={[
            "Registro profissional é obrigatório.",
            "Especialidade define os tipos de consulta.",
            "Disponibilidade evita conflitos de agenda.",
          ]}
        />
      </div>
    </section>
  );
}
