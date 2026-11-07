import DataTable from '../components/DataTable/DataTable';
import PageHeader from '../components/PageHeader/PageHeader';
import StatusBadge from '../components/StatusBadge/StatusBadge';

export default function Consultas() {
  return (
    <section>
      <PageHeader
        eyebrow="Atendimentos"
        title="Agenda de consultas"
        description="Pacientes e profissionais vinculados em cada horário."
        action={{ href: "/nova-consulta", label: "＋ Agendar consulta" }}
      />
      <DataTable
        toolbar={
          <>
            <input placeholder="⌕  Buscar por paciente ou profissional" aria-label="Buscar por paciente ou profissional" />
            <button type="button">14 mai 2024⌄</button>
            <button type="button">Todos os status⌄</button>
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
          <tr>
            <td>
              <b>08:30</b>
            </td>
            <td>Mariana Alves</td>
            <td>Dra. Helena Martins</td>
            <td>Cardiologia</td>
            <td>
              <StatusBadge tone="ok">Confirmada</StatusBadge>
            </td>
          </tr>
          <tr>
            <td>
              <b>10:00</b>
            </td>
            <td>Rafael Costa</td>
            <td>Dr. Gustavo Nunes</td>
            <td>Ortopedia</td>
            <td>
              <StatusBadge tone="wait">Aguardando</StatusBadge>
            </td>
          </tr>
          <tr>
            <td>
              <b>11:30</b>
            </td>
            <td>Aline Ribeiro</td>
            <td>Dra. Paula Mendes</td>
            <td>Clínica geral</td>
            <td>
              <StatusBadge tone="ok">Confirmada</StatusBadge>
            </td>
          </tr>
        </tbody>
      </DataTable>
    </section>
  );
}
