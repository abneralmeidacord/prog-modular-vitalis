import { useState } from 'react';
import FormCard from '../components/FormCard/FormCard';
import PageHeader from '../components/PageHeader/PageHeader';
import RulesCard from '../components/RulesCard/RulesCard';

export default function NovoProfissional() {
  const [categoria, setCategoria] = useState('CLINICO_GERAL');

  return (
    <section>
      <PageHeader
        eyebrow="Cadastros / Profissionais"
        title="Novo profissional"
        description="Registre a equipe que poderá assumir atendimentos."
        action={{ href: "/profissionais", label: "← Voltar para profissionais", variant: "ghost" }}
      />
      <FormCard endpoint="/profissionais" destination="/profissionais" description="O profissional ficará disponível para vínculo em consultas." submitLabel="Salvar profissional">
        <label>
          Nome completo
          <input name="nome" required placeholder="Ex.: Dra. Helena Martins" />
        </label>
        <label>
          CRM / registro
          <input name="registro" required placeholder="Ex.: CRM 48291" />
        </label>
        <label>
          Categoria
          <select name="categoria" value={categoria} onChange={event => setCategoria(event.target.value)}>
            <option value="CLINICO_GERAL">Clínico geral</option>
            <option value="ESPECIALISTA">Especialista</option>
            <option value="CIRURGIAO">Cirurgião</option>
          </select>
        </label>
        {categoria === 'ESPECIALISTA' && (
          <label>Especialidade<input name="especialidade" required placeholder="Ex.: Cardiologia" /></label>
        )}
        {categoria === 'CIRURGIAO' && (
          <label>Área cirúrgica<input name="areaCirurgica" required placeholder="Ex.: Cirurgia geral" /></label>
        )}
        <label>Telefone<input name="telefone" type="tel" required /></label>
        <label>
          E-mail profissional
          <input name="email" type="email" required placeholder="profissional@hospital.com" />
        </label>
      </FormCard>
      <RulesCard
        title="Regras desta tela"
        items={[
          "Nome, registro, telefone e e-mail são obrigatórios.",
          "Um profissional não pode ter dois atendimentos no mesmo horário.",
        ]}
      />
    </section>
  );
}
