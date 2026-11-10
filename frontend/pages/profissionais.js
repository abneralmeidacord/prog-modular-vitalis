import { useApi } from '../lib/api';
import { iniciais } from '../lib/format';
import RequestStatus from '../components/RequestStatus/RequestStatus';
import Avatar from '../components/Avatar/Avatar';
import PageHeader from '../components/PageHeader/PageHeader';
import RulesCard from '../components/RulesCard/RulesCard';
import styles from '../styles/PageContent.module.css';

export default function Profissionais() {
  const profissionais = useApi('/profissionais');

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
            {profissionais.data && <span className={styles.badge}>{profissionais.data.filter(item => item.disponivel).length} disponíveis</span>}
          </div>
          <div className={styles.team}>
            <RequestStatus request={profissionais} empty="Nenhum profissional cadastrado." />
            {profissionais.data?.map(profissional => (
              <p key={profissional.id}>
                <Avatar>{iniciais(profissional.nome)}</Avatar>
                <span><b>{profissional.nome}</b><small>{profissional.area} · {profissional.registro}</small></span>
                <span className={styles.availability}>{profissional.disponivel ? 'Disponível' : 'Indisponível'}</span>
              </p>
            ))}
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
