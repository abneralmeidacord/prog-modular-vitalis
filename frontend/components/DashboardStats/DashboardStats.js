import styles from './DashboardStats.module.css';

export default function DashboardStats() {
  return (
    <section className={styles.stats}>
      <article>
        <span>Pacientes ativos</span>
        <strong>248</strong>
        <small className={styles.up}>
          ↑ 8,2%
          <i>vs. mês anterior</i>
        </small>
      </article>
      <article>
        <span>Consultas hoje</span>
        <strong>18</strong>
        <small className={styles.up}>
          ↑ 12,5%
          <i>vs. terça passada</i>
        </small>
      </article>
      <article>
        <span>Internações ativas</span>
        <strong>12</strong>
        <small>
          → estável
          <i>últimos 7 dias</i>
        </small>
      </article>
      <article>
        <span>Ocupação dos quartos</span>
        <strong>
          76
          <sup>%</sup>
        </strong>
        <div className={styles.progress}>
          <i />
        </div>
        <small>
          27 de 36 leitos
          <b className={styles.warning}>atenção</b>
        </small>
      </article>
    </section>
  );
}
