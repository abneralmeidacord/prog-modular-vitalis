import CardHeader from '../CardHeader/CardHeader';
import StatusBadge from '../StatusBadge/StatusBadge';
import { statusConsulta } from '../../lib/format';
import styles from './Agenda.module.css';

export default function Agenda({ consultas, pacientes }) {
  const nomes = new Map(pacientes.map(paciente => [paciente.id, paciente.nome]));

  return (
    <article className={'surface ' + styles.schedule}>
      <CardHeader title="Agenda de hoje" description="Consultas programadas"
        link={{ href: '/consultas', label: 'Ver agenda completa →' }} />
      <div className={styles.appointments}>
        {consultas.length === 0 && <p>Nenhuma consulta para hoje.</p>}
        {consultas.map(({ id, consulta }) => (
          <div key={id}>
            <time>{consulta.horario.slice(0, 5)}</time>
            <i />
            <span>
              <b>{nomes.get(consulta.pacienteId) || 'Paciente #' + consulta.pacienteId}</b>
              <small>{consulta.profissional.nome} · {consulta.profissional.area}</small>
            </span>
            <StatusBadge tone={statusConsulta[consulta.status]?.tone || 'info'}>
              {statusConsulta[consulta.status]?.label || consulta.status}
            </StatusBadge>
          </div>
        ))}
      </div>
    </article>
  );
}
