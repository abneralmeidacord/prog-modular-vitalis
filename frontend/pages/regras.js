import PageHeader from '../components/PageHeader/PageHeader';
import RulesCard from '../components/RulesCard/RulesCard';
import styles from '../styles/PageContent.module.css';

export default function Regras() {
  return (
    <section>
      <PageHeader
        eyebrow="Governança"
        title="Regras de negócio"
        description="Referência para operação segura e consistente do sistema."
      />
      <div className={styles.rulesGrid}>
        <RulesCard
          title="Pacientes"
          icon="01"
          as="article"
          items={[
            "CPF identifica unicamente cada paciente.",
            "Um paciente pode ter várias consultas e internações.",
            "O histórico nunca é apagado após uma alta.",
          ]}
        />
        <RulesCard
          title="Consultas"
          icon="02"
          as="article"
          items={[
            "Toda consulta exige paciente e profissional.",
            "Um profissional não pode ter dois atendimentos no mesmo horário.",
            "Status registra confirmação, espera ou cancelamento.",
          ]}
        />
        <RulesCard
          title="Internações"
          icon="03"
          as="article"
          items={[
            "A internação exige quarto disponível.",
            "A ocupação não pode ultrapassar a capacidade cadastrada.",
            "A alta libera a vaga e mantém o evento no histórico.",
          ]}
        />
        <RulesCard
          title="Relacionamentos"
          icon="04"
          as="article"
          items={[
            "Profissional pode atender vários pacientes.",
            "Quarto pode receber vários pacientes em momentos diferentes.",
            "Todo vínculo deve apontar para entidades cadastradas.",
          ]}
        />
      </div>
    </section>
  );
}
