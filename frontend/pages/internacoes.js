import Link from 'next/link';
import { useRouter } from 'next/router';
import { useApi } from '../lib/api';
import { formatarData, iniciais } from '../lib/format';
import Avatar from '../components/Avatar/Avatar';
import DataTable from '../components/DataTable/DataTable';
import PageHeader from '../components/PageHeader/PageHeader';
import StatusBadge from '../components/StatusBadge/StatusBadge';
import styles from '../styles/PageContent.module.css';

export default function Internacoes() {
  const { query } = useRouter();
  const filtro = typeof query.quartoId === 'string' ? '?ativas=true&quartoId=' + encodeURIComponent(query.quartoId) : '';
  const internacoes = useApi('/internacoes' + filtro);

  return (
    <section>
      <PageHeader
        eyebrow="Atendimentos"
        title="Internações"
        description="Admissão, quarto, profissional responsável e previsão de alta."
        action={{ href: "/nova-internacao", label: "＋ Nova internação" }}
      />
      <DataTable request={internacoes} empty="Nenhuma internação encontrada." colSpan={5}>
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
          {internacoes.data?.map(internacao => (
            <tr key={internacao.id}>
              <td><Link className={styles.person} href={'/pacientes/' + internacao.paciente.id}>
                <Avatar size="small">{iniciais(internacao.paciente.nome)}</Avatar><b>{internacao.paciente.nome}</b>
              </Link></td>
              <td>{internacao.profissional.nome}</td>
              <td>{internacao.quarto.numero}{internacao.quarto.andar ? ' · ' + internacao.quarto.andar : ''}</td>
              <td>{formatarData(internacao.dataPrevistaAlta)}</td>
              <td><StatusBadge tone={internacao.ativa ? 'info' : 'ok'}>{internacao.ativa ? 'Ativa' : 'Encerrada'}</StatusBadge></td>
            </tr>
          ))}
        </tbody>
      </DataTable>
    </section>
  );
}
