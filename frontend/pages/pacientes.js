import Link from 'next/link';
import Avatar from '../components/Avatar/Avatar';
import DataTable from '../components/DataTable/DataTable';
import PageHeader from '../components/PageHeader/PageHeader';
import StatusBadge from '../components/StatusBadge/StatusBadge';
import styles from '../styles/PageContent.module.css';

export default function Pacientes() {
  return (
    <section>
      <PageHeader
        eyebrow="Cadastro de pacientes"
        title="Pacientes"
        description="Consulte fichas, vínculos, atendimentos e histórico médico."
        action={{ href: "/novo-paciente", label: "＋ Cadastrar paciente" }}
      />
      <DataTable
        toolbar={
          <>
            <input aria-label="Buscar paciente" placeholder="⌕  Buscar por nome ou CPF" />
            <button type="button">Todos os pacientes⌄</button>
            <button type="button">Mais recentes⌄</button>
          </>
        }
      >
        <thead>
          <tr>
            <th>Paciente</th>
            <th>CPF</th>
            <th>Último atendimento</th>
            <th>Situação</th>
            <th>Ação</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>
              <span className={styles.person}>
                <Avatar tone="violet" size="small">MA</Avatar>
                <b>Mariana Alves</b>
              </span>
            </td>
            <td>•••.482.190-••</td>
            <td>Consulta hoje, 08:30</td>
            <td>
              <StatusBadge tone="ok">Ativo</StatusBadge>
            </td>
            <td>
              <Link href="/ficha-mariana">Abrir ficha</Link>
            </td>
          </tr>
          <tr>
            <td>
              <span className={styles.person}>
                <Avatar size="small">RC</Avatar>
                <b>Rafael Costa</b>
              </span>
            </td>
            <td>•••.119.704-••</td>
            <td>Internado · Quarto 204</td>
            <td>
              <StatusBadge tone="ok">Ativo</StatusBadge>
            </td>
            <td>
              <Link href="/ficha-rafael">Abrir ficha</Link>
            </td>
          </tr>
          <tr>
            <td>
              <span className={styles.person}>
                <Avatar tone="peach" size="small">AR</Avatar>
                <b>Aline Ribeiro</b>
              </span>
            </td>
            <td>•••.832.551-••</td>
            <td>Consulta há 4 dias</td>
            <td>
              <StatusBadge tone="ok">Ativo</StatusBadge>
            </td>
            <td>Ficha indisponível</td>
          </tr>
        </tbody>
      </DataTable>
    </section>
  );
}
