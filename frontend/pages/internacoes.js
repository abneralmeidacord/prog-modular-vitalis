import Avatar from '../components/Avatar/Avatar';
import DataTable from '../components/DataTable/DataTable';
import PageHeader from '../components/PageHeader/PageHeader';
import StatusBadge from '../components/StatusBadge/StatusBadge';
import styles from '../styles/PageContent.module.css';

export default function Internacoes() {
  return (
    <section>
      <PageHeader
        eyebrow="Atendimentos"
        title="Internações"
        description="Admissão, quarto, profissional responsável e previsão de alta."
        action={{ href: "/nova-internacao", label: "＋ Nova internação" }}
      />
      <DataTable>
        <thead>
          <tr>
            <th>Paciente</th>
            <th>Profissional</th>
            <th>Quarto</th>
            <th>Previsão de alta</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>
              <span className={styles.person}>
                <Avatar size="small">RC</Avatar>
                <b>Rafael Costa</b>
              </span>
            </td>
            <td>Dra. Paula Mendes</td>
            <td>204 · 2º andar</td>
            <td>18 mai 2024</td>
            <td>
              <StatusBadge tone="info">Em observação</StatusBadge>
            </td>
          </tr>
          <tr>
            <td>
              <span className={styles.person}>
                <Avatar tone="peach" size="small">LS</Avatar>
                <b>Lucas Soares</b>
              </span>
            </td>
            <td>Dr. André Freitas</td>
            <td>312 · 3º andar</td>
            <td>21 mai 2024</td>
            <td>
              <StatusBadge tone="wait">UTI</StatusBadge>
            </td>
          </tr>
        </tbody>
      </DataTable>
    </section>
  );
}
