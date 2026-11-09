export const tiposQuarto = {
  ENFERMARIA: { label: 'Enfermaria', capacidade: 8 },
  ISOLAMENTO: { label: 'Isolamento', capacidade: 1 },
  UTI: { label: 'UTI', capacidade: 1 },
  SUITE: { label: 'Suíte', capacidade: 2 },
};

export const statusConsulta = {
  AGENDADA: { label: 'Agendada', tone: 'wait' },
  CONFIRMADA: { label: 'Confirmada', tone: 'ok' },
  EM_ATENDIMENTO: { label: 'Em atendimento', tone: 'info' },
  CONCLUIDA: { label: 'Concluída', tone: 'ok' },
  CANCELADA: { label: 'Cancelada', tone: 'danger' },
  NAO_COMPARECEU: { label: 'Não compareceu', tone: 'danger' },
};

export function iniciais(nome) {
  return nome.split(/\s+/).slice(0, 2).map(parte => parte[0]).join('').toUpperCase();
}

export function formatarData(data) {
  return data ? data.slice(0, 10).split('-').reverse().join('/') : '—';
}

export function calcularOcupacao(quartos) {
  const capacidade = quartos.reduce((total, quarto) => total + quarto.capacidade, 0);
  const ocupados = quartos.reduce((total, quarto) => total + quarto.ocupados, 0);
  return { capacidade, ocupados, percentual: capacidade ? Math.round(100 * ocupados / capacidade) : 0 };
}
