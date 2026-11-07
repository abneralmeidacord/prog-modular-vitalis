import Link from 'next/link';
import styles from './QuickActions.module.css';

export default function QuickActions() {
  return (
    <section className={styles.actionPanel}>
      <div className={styles.actionHeading}>
        <div>
          <p className="eyebrow">Ações rápidas</p>
          <h2>O que você precisa fazer?</h2>
          <p>Acesse diretamente os fluxos mais usados da operação.</p>
        </div>
        <Link href="/regras">Consultar regras →</Link>
      </div>
      <div className={styles.quickLinks}>
        <Link href="/novo-paciente" className={`surface ${styles.quick}`}>
          <b>＋</b>
          <span>
            <strong>Cadastrar paciente</strong>
            <small>Nova ficha cadastral</small>
          </span>
          →
        </Link>
        <Link href="/novo-profissional" className={`surface ${styles.quick}`}>
          <b>＋</b>
          <span>
            <strong>Cadastrar profissional</strong>
            <small>Equipe e especialidades</small>
          </span>
          →
        </Link>
        <Link href="/novo-quarto" className={`surface ${styles.quick}`}>
          <b>＋</b>
          <span>
            <strong>Adicionar quarto</strong>
            <small>Capacidade e leitos</small>
          </span>
          →
        </Link>
        <Link href="/nova-consulta" className={`surface ${styles.quick}`}>
          <b>◷</b>
          <span>
            <strong>Agendar consulta</strong>
            <small>Vincule paciente e profissional</small>
          </span>
          →
        </Link>
        <Link href="/nova-internacao" className={`surface ${styles.quick}`}>
          <b>⌂</b>
          <span>
            <strong>Registrar internação</strong>
            <small>Use apenas leito disponível</small>
          </span>
          →
        </Link>
        <Link href="/historico" className={`surface ${styles.quick}`}>
          <b>≡</b>
          <span>
            <strong>Ver histórico médico</strong>
            <small>Eventos e prontuários</small>
          </span>
          →
        </Link>
      </div>
    </section>
  );
}
