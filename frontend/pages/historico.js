import { useRouter } from 'next/router';
import CadastroSelect from '../components/CadastroSelect/CadastroSelect';
import Button from '../components/Button/Button';
import PageHeader from '../components/PageHeader/PageHeader';
import RequestStatus from '../components/RequestStatus/RequestStatus';
import { useApi } from '../lib/api';
import styles from '../components/FormCard/FormCard.module.css';

export default function Historico() {
  const router = useRouter();
  const pacientes = useApi('/pacientes');

  function abrirFicha(event) {
    event.preventDefault();
    const id = new FormData(event.currentTarget).get('pacienteId');
    if (id) router.push('/pacientes/' + id);
  }

  return (
    <section>
      <PageHeader eyebrow="Prontuários" title="Histórico médico"
        description="Selecione um paciente para consultar seus atendimentos." />
      <RequestStatus request={pacientes} empty="Nenhum paciente cadastrado." />
      {Boolean(pacientes.data?.length) && (
        <form className={'surface ' + styles.formCard} onSubmit={abrirFicha}>
          <div className={styles.formGrid}>
            <CadastroSelect label="Paciente" name="pacienteId" options={pacientes.data} />
          </div>
          <div className={styles.formFooter}>
            <Button type="submit">Abrir histórico</Button>
          </div>
        </form>
      )}
    </section>
  );
}
