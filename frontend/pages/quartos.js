import { useApi } from '../lib/api';
import { calcularOcupacao, tiposQuarto } from '../lib/format';
import Link from 'next/link';
import DataTable from '../components/DataTable/DataTable';
import PageHeader from '../components/PageHeader/PageHeader';
import StatusBadge from '../components/StatusBadge/StatusBadge';

export default function Quartos() {
  const quartos = useApi('/quartos');
  const { capacidade, ocupados, percentual } = calcularOcupacao(quartos.data || []);

  return (
    <section>
      <PageHeader
        eyebrow="Estrutura hospitalar"
        title="Quartos e leitos"
        description="Controle de capacidade, ocupação e disponibilidade por andar."
        action={{ href: "/novo-quarto", label: "＋ Adicionar quarto" }}
      />
      <DataTable request={quartos} empty="Nenhum quarto cadastrado." colSpan={5}
        summary={quartos.data &&
          <>
            <strong>
              {ocupados}
              <small>leitos ocupados</small>
            </strong>
            <strong>
              {capacidade - ocupados}
              <small>leitos disponíveis</small>
            </strong>
            <strong>
              {percentual}%
              <small>ocupação geral</small>
            </strong>
          </>
        }
      >
        <thead>
          <tr>
            <th>Quarto</th>
            <th>Tipo e localização</th>
            <th>Leitos</th>
            <th>Situação</th>
            <th>Ação</th>
          </tr>
        </thead>
        <tbody>
          {quartos.data?.map(quarto => (
            <tr key={quarto.id}>
              <td><b>{quarto.numero}</b></td>
              <td>{tiposQuarto[quarto.tipo].label}{quarto.andar ? ' · ' + quarto.andar : ''}</td>
              <td>{quarto.ocupados} de {quarto.capacidade} ocupados</td>
              <td><StatusBadge tone={quarto.disponivel ? 'ok' : 'info'}>{quarto.disponivel ? 'Disponível' : 'Ocupado'}</StatusBadge></td>
              <td>{quarto.disponivel ? (
                <Link href={'/nova-internacao?quartoId=' + quarto.id}>Internar</Link>
              ) : (
                <Link href={'/internacoes?quartoId=' + quarto.id}>Ver ocupantes</Link>
              )}</td>
            </tr>
          ))}
        </tbody>
      </DataTable>
    </section>
  );
}
