import { useState } from 'react';
import { useApi } from '../lib/api';
import { formatarData, statusConsulta } from '../lib/format';
import DataTable from '../components/DataTable/DataTable';
import PageHeader from '../components/PageHeader/PageHeader';
import StatusBadge from '../components/StatusBadge/StatusBadge';

export default function Consultas() {
  const consultas = useApi('/consultas');
  const pacientes = useApi('/pacientes');
  const [busca, setBusca] = useState('');
  const [data, setData] = useState('');
  const [status, setStatus] = useState('');
  const nomes = new Map((pacientes.data || []).map(paciente => [paciente.id, paciente.nome]));
  const termo = busca.trim().toLowerCase();
  const filtradas = (consultas.data || []).filter(({ consulta }) =>
    (!data || consulta.data === data) && (!status || consulta.status === status) &&
    [nomes.get(consulta.pacienteId), consulta.profissional.nome]
      .some(nome => nome?.toLowerCase().includes(termo)));

  return (
    <section>
      <PageHeader
        eyebrow="Atendimentos"
        title="Agenda de consultas"
        description="Pacientes e profissionais vinculados em cada horário."
        action={{ href: "/nova-consulta", label: "＋ Agendar consulta" }}
      />
      <DataTable request={{ ...consultas, data: filtradas }} empty="Nenhuma consulta encontrada." colSpan={5}
        toolbar={
          <>
            <input aria-label="Buscar por paciente ou profissional" placeholder="Buscar por paciente ou profissional"
              value={busca} onChange={event => setBusca(event.target.value)} />
            <input aria-label="Filtrar por data" type="date" value={data} onChange={event => setData(event.target.value)} />
            <select aria-label="Filtrar por status" value={status} onChange={event => setStatus(event.target.value)}>
              <option value="">Todos os status</option>
              {Object.entries(statusConsulta).map(([valor, item]) => <option key={valor} value={valor}>{item.label}</option>)}
            </select>
          </>
        }
      >
        <thead>
          <tr>
            <th>Horário</th>
            <th>Paciente</th>
            <th>Profissional</th>
            <th>Especialidade</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          {filtradas.map(({ id, consulta }) => (
            <tr key={id}>
              <td><b>{formatarData(consulta.data)} · {consulta.horario.slice(0, 5)}</b></td>
              <td>{nomes.get(consulta.pacienteId) || 'Paciente #' + consulta.pacienteId}</td>
              <td>{consulta.profissional.nome}</td>
              <td>{consulta.especialidade || consulta.profissional.area}</td>
              <td><StatusBadge tone={statusConsulta[consulta.status]?.tone || 'info'}>
                {statusConsulta[consulta.status]?.label || consulta.status}
              </StatusBadge></td>
            </tr>
          ))}
        </tbody>
      </DataTable>
    </section>
  );
}
