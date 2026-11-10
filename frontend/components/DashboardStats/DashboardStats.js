import { calcularOcupacao } from '../../lib/format';
import styles from './DashboardStats.module.css';

export default function DashboardStats({ pacientes, consultas, internacoes, quartos }) {
  const { capacidade, ocupados, percentual } = calcularOcupacao(quartos);
  return (
    <section className={styles.stats}>
      <article><span>Pacientes ativos</span><strong>{pacientes.filter(item => item.ativo).length}</strong></article>
      <article><span>Consultas hoje</span><strong>{consultas.length}</strong></article>
      <article><span>Internações ativas</span><strong>{internacoes.length}</strong></article>
      <article>
        <span>Ocupação dos quartos</span>
        <strong>{percentual}<sup>%</sup></strong>
        <div className={styles.progress}><i style={{ width: percentual + '%' }} /></div>
        <small>{ocupados} de {capacidade} leitos</small>
      </article>
    </section>
  );
}
