import { useRouter } from 'next/router';
import CadastroSelect from '../components/CadastroSelect/CadastroSelect';
import RequestStatus from '../components/RequestStatus/RequestStatus';
import { useApi } from '../lib/api';
import { tiposQuarto } from '../lib/format';
import FormCard from '../components/FormCard/FormCard';
import PageHeader from '../components/PageHeader/PageHeader';
import RulesCard from '../components/RulesCard/RulesCard';

export default function NovaInternacao() {
  const { query } = useRouter();
  const pacientes = useApi('/pacientes');
  const profissionais = useApi('/profissionais');
  const quartos = useApi('/quartos/disponiveis');
  const disponiveis = (pacientes.data || []).filter(paciente => paciente.ativo && !paciente.internado);
  const quartoId = quartos.data?.find(quarto => String(quarto.id) === query.quartoId)?.id ?? '';

  function toPayload(fields) {
    return {
      ...fields,
      pacienteId: Number(fields.pacienteId),
      profissionalId: Number(fields.profissionalId),
      quartoId: Number(fields.quartoId),
      dataPrevistaAlta: fields.dataPrevistaAlta || null,
    };
  }

  return (
    <section>
      <PageHeader
        eyebrow="Atendimentos / Internações"
        title="Registrar internação"
        description="Associe paciente, profissional responsável e leito disponível."
        action={{ href: "/internacoes", label: "← Voltar para internações", variant: "ghost" }}
      />
      <RequestStatus request={{ ...pacientes, data: disponiveis }} empty="Nenhum paciente disponível para internação." />
      <RequestStatus request={profissionais} empty="Cadastre um profissional responsável." />
      <RequestStatus request={quartos} empty="Nenhum quarto com leito disponível." />
      <FormCard endpoint="/internacoes" destination="/internacoes" toPayload={toPayload}
        disabled={!disponiveis.length || !profissionais.data?.length || !quartos.data?.length} description="Somente quartos com leito disponível poderão ser selecionados." submitLabel="Confirmar internação">
        <CadastroSelect label="Paciente" name="pacienteId" options={disponiveis} />
        <CadastroSelect label="Profissional responsável" name="profissionalId" options={profissionais.data || []} />
        <CadastroSelect key={quartoId} label="Quarto disponível" name="quartoId" options={quartos.data || []}
          defaultValue={quartoId} describe={quarto => quarto.numero + ' · ' + tiposQuarto[quarto.tipo].label} />
        <label>
          Previsão de alta
          <input name="dataPrevistaAlta" type="date" />
        </label>
        <label data-wide="true">
          Motivo da internação
          <textarea name="observacoes" placeholder="Descrição inicial e observações" />
        </label>
      </FormCard>
      <RulesCard
        title="Regras desta tela"
        items={[
          "Paciente e profissional são obrigatórios.",
          "O quarto precisa estar disponível e dentro da capacidade.",
          "A alta libera a vaga e preserva o histórico.",
        ]}
      />
    </section>
  );
}
