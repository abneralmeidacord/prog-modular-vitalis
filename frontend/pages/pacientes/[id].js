import { useRouter } from 'next/router';
import Avatar from '../../components/Avatar/Avatar';
import Button from '../../components/Button/Button';
import CardHeader from '../../components/CardHeader/CardHeader';
import PageHeader from '../../components/PageHeader/PageHeader';
import PatientCard from '../../components/PatientCard/PatientCard';
import RequestStatus from '../../components/RequestStatus/RequestStatus';
import StatusBadge from '../../components/StatusBadge/StatusBadge';
import Timeline from '../../components/Timeline/Timeline';
import { useApi } from '../../lib/api';
import { formatarData, iniciais, statusConsulta } from '../../lib/format';
import styles from '../../styles/PageContent.module.css';

export default function FichaPaciente() {
  const { query } = useRouter();
  const id = typeof query.id === 'string' ? encodeURIComponent(query.id) : '';
  const cadastro = useApi(id && '/pacientes/' + id);
  const consultas = useApi(id && '/consultas?pacienteId=' + id);
  const internacoes = useApi(id && '/pacientes/' + id + '/internacoes');
  const paciente = cadastro.data;
  const historico = {
    loading: consultas.loading || internacoes.loading,
    error: consultas.error || internacoes.error,
    data: [
      ...(consultas.data || []).map(({ id, consulta }) => ({
        key: 'consulta-' + id,
        when: consulta.dataHoraInicio,
        title: 'Consulta · ' + consulta.profissional.area,
        description: [statusConsulta[consulta.status]?.label, consulta.motivo, consulta.observacoes]
          .filter(Boolean).join(' · '),
      })),
      ...(internacoes.data || []).map(internacao => ({
        key: 'internacao-' + internacao.id,
        when: internacao.dataEntrada,
        title: 'Internação · Quarto ' + internacao.quarto.numero,
        description: internacao.observacoes.join(' · ') || (internacao.ativa ? 'Em internação' : 'Encerrada'),
      })),
    ].sort((a, b) => b.when.localeCompare(a.when)),
  };

  return (
    <section>
      <PageHeader eyebrow="Pacientes / Ficha clínica" title={paciente?.nome || 'Ficha do paciente'}
        description="Cadastro, vínculos e histórico de atendimentos."
        action={{ href: '/pacientes', label: '← Voltar para pacientes', variant: 'ghost' }} />
      <RequestStatus request={cadastro} />
      {paciente && (
        <div className={styles.detailGrid}>
          <PatientCard>
            <Avatar size="large">{iniciais(paciente.nome)}</Avatar>
            <h2>{paciente.nome}</h2>
            <p>CPF {paciente.cpf}</p>
            <hr />
            <dl>
              <dt>Data de nascimento</dt><dd>{formatarData(paciente.dataNascimento)}</dd>
              <dt>Telefone</dt><dd>{paciente.telefone || '—'}</dd>
              <dt>E-mail</dt><dd>{paciente.email || '—'}</dd>
              <dt>Endereço</dt><dd>{paciente.endereco || '—'}</dd>
              <dt>Situação</dt>
              <dd><StatusBadge tone={paciente.ativo ? 'ok' : 'danger'}>{paciente.ativo ? 'Ativo' : 'Inativo'}</StatusBadge></dd>
            </dl>
            {paciente.observacoes && <p>{paciente.observacoes}</p>}
            {paciente.ativo && <Button href={'/nova-consulta?pacienteId=' + id} variant="secondary">+ Nova consulta</Button>}
          </PatientCard>
          <article className="surface">
            <CardHeader title="Histórico de atendimentos" description="Consultas e internações vinculadas ao paciente" />
            <RequestStatus request={historico} empty="Nenhum atendimento registrado." />
            <Timeline events={historico.data.map(event => ({ ...event, date: formatarData(event.when) }))} />
          </article>
        </div>
      )}
    </section>
  );
}
