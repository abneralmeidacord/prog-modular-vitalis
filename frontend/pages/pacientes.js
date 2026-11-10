import { useState } from 'react';
import { useApi } from '../lib/api';
import { iniciais } from '../lib/format';
import Link from 'next/link';
import Avatar from '../components/Avatar/Avatar';
import DataTable from '../components/DataTable/DataTable';
import PageHeader from '../components/PageHeader/PageHeader';
import StatusBadge from '../components/StatusBadge/StatusBadge';
import styles from '../styles/PageContent.module.css';

export default function Pacientes() {
  const [busca, setBusca] = useState('');
  const termo = busca.trim();
  const pacientes = useApi(termo ? '/pacientes?busca=' + encodeURIComponent(termo) : '/pacientes');

  return (
    <section>
      <PageHeader
        eyebrow="Cadastro de pacientes"
        title="Pacientes"
        description="Consulte fichas, vínculos, atendimentos e histórico médico."
        action={{ href: "/novo-paciente", label: "＋ Cadastrar paciente" }}
      />
      <DataTable request={pacientes} empty="Nenhum paciente encontrado." colSpan={5}
        toolbar={<input aria-label="Buscar paciente" placeholder="Buscar por nome ou CPF"
          value={busca} onChange={event => setBusca(event.target.value)} />}
      >
        <thead>
          <tr>
            <th>Paciente</th>
            <th>CPF</th>
            <th>Internação</th>
            <th>Situação</th>
            <th>Ação</th>
          </tr>
        </thead>
        <tbody>
          {pacientes.data?.map(paciente => (
            <tr key={paciente.id}>
              <td><span className={styles.person}>
                <Avatar size="small">{iniciais(paciente.nome)}</Avatar><b>{paciente.nome}</b>
              </span></td>
              <td>{paciente.cpf}</td>
              <td>{paciente.internado ? 'Internado' : '—'}</td>
              <td><StatusBadge tone={paciente.ativo ? 'ok' : 'danger'}>{paciente.ativo ? 'Ativo' : 'Inativo'}</StatusBadge></td>
              <td><Link href={'/pacientes/' + paciente.id}>Abrir ficha</Link></td>
            </tr>
          ))}
        </tbody>
      </DataTable>
    </section>
  );
}
