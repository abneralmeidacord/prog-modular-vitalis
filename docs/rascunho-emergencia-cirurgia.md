# Emergência e cirurgia — rascunho da Sprint 2

## Classes e relacionamentos

`Atendimento` é a classe-base. Ela guarda o paciente, o profissional responsável e as observações gerais.

- `AtendimentoEmergencial` herda de `Atendimento` e acrescenta prioridade e estado.
- `ProcedimentoCirurgico` herda de `Atendimento` e acrescenta descrição, tipo, observações pré e pós-operatórias e data e hora previstas e efetivas. O profissional responsável deve ser um cirurgião.

## Prioridades e estados

- **Emergência** — prioridades `BAIXA`, `MODERADA`, `ALTA` e `CRITICA`. Fluxo `AGUARDANDO_AVALIACAO → INICIADO → CONCLUIDO`.
- **Cirurgia** — fluxo `AGENDADO → EM_ANDAMENTO → CONCLUIDO`. O estado `CANCELADO` também existe. A data e hora efetivas são registradas ao iniciar.

## Cenários de teste

- Criar uma emergência com cada uma das quatro prioridades e verificar a classificação registrada.
- Tentar concluir uma cirurgia agendada e verificar que a operação é rejeitada e o estado continua `AGENDADO`.
- Iniciar e concluir uma cirurgia e verificar o horário efetivo e o estado final.
- Verificar se o procedimento mantém os vínculos com o paciente e o cirurgião e rejeita um responsável de outra categoria.

## Pontos para alinhar com o grupo

- O responsável pela emergência é obrigatório desde o registro ou pode ser atribuído ao iniciar?
- A cirurgia pode ser cancelada depois de iniciada?