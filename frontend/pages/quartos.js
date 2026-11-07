import Link from 'next/link';
import DataTable from '../components/DataTable/DataTable';
import PageHeader from '../components/PageHeader/PageHeader';
import StatusBadge from '../components/StatusBadge/StatusBadge';

export default function Quartos() {
  return (
    <section>
      <PageHeader
        eyebrow="Estrutura hospitalar"
        title="Quartos e leitos"
        description="Controle de capacidade, ocupação e disponibilidade por andar."
        action={{ href: "/novo-quarto", label: "＋ Adicionar quarto" }}
      />
      <DataTable
        summary={
          <>
            <strong>
              27
              <small>leitos ocupados</small>
            </strong>
            <strong>
              9
              <small>leitos disponíveis</small>
            </strong>
            <strong>
              76%
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
          <tr>
            <td>
              <b>101</b>
            </td>
            <td>Enfermaria · 1º andar</td>
            <td>1 de 2 ocupados</td>
            <td>
              <StatusBadge tone="ok">Disponível</StatusBadge>
            </td>
            <td>
              <Link href="/nova-internacao">Internar</Link>
            </td>
          </tr>
          <tr>
            <td>
              <b>204</b>
            </td>
            <td>Enfermaria · 2º andar</td>
            <td>2 de 2 ocupados</td>
            <td>
              <StatusBadge tone="info">Ocupado</StatusBadge>
            </td>
            <td>
              <Link href="/ficha-rafael">Ver ocupantes</Link>
            </td>
          </tr>
          <tr>
            <td>
              <b>305</b>
            </td>
            <td>Isolamento · 3º andar</td>
            <td>0 de 1 ocupados</td>
            <td>
              <StatusBadge tone="ok">Disponível</StatusBadge>
            </td>
            <td>
              <Link href="/nova-internacao">Internar</Link>
            </td>
          </tr>
        </tbody>
      </DataTable>
    </section>
  );
}
