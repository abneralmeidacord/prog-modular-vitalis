import CardHeader from '../CardHeader/CardHeader';
import StatusBadge from '../StatusBadge/StatusBadge';
import styles from './Agenda.module.css';

export default function Agenda() {
  return (
    <article className={`surface ${styles.schedule}`}>
      <CardHeader
        title="Agenda de hoje"
        description="Consultas e atendimentos programados"
        link={{ href: "/consultas", label: "Ver agenda completa →" }}
      />
      <div className={styles.dates}>
        <button type="button" aria-label="Dias anteriores">‹</button>
        <span>
          14
          <small>MAI · TER</small>
        </span>
        <b>
          15
          <small>MAI · QUA</small>
        </b>
        <span>
          16
          <small>MAI · QUI</small>
        </span>
        <span>
          17
          <small>MAI · SEX</small>
        </span>
        <button type="button" aria-label="Próximos dias">›</button>
      </div>
      <div className={styles.appointments}>
        <div>
          <time>08:30</time>
          <i />
          <span>
            <b>Mariana Alves</b>
            <small>Cardiologia · Dra. Helena Martins</small>
          </span>
          <StatusBadge tone="ok">Confirmada</StatusBadge>
        </div>
        <div>
          <time>10:00</time>
          <i />
          <span>
            <b>Rafael Costa</b>
            <small>Ortopedia · Dr. Gustavo Nunes</small>
          </span>
          <StatusBadge tone="wait">Aguardando</StatusBadge>
        </div>
        <div>
          <time>11:30</time>
          <i />
          <span>
            <b>Aline Ribeiro</b>
            <small>Clínica geral · Dra. Paula Mendes</small>
          </span>
          <StatusBadge tone="ok">Confirmada</StatusBadge>
        </div>
        <div>
          <time>14:00</time>
          <i />
          <span>
            <b>João Vitor Lima</b>
            <small>Neurologia · Dr. André Freitas</small>
          </span>
          <StatusBadge tone="danger">Cancelada</StatusBadge>
        </div>
      </div>
    </article>
  );
}
