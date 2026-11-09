import { useEffect, useState } from 'react';

export async function apiRequest(path, { method = 'GET', data, signal } = {}) {
  const response = await fetch('/api/vitalis' + path, {
    method,
    signal,
    ...(data === undefined ? {} : {
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    }),
  }).catch(error => {
    if (error.name === 'AbortError') throw error;
    throw new Error('Não foi possível conectar ao servidor do Vitalis.');
  });

  const result = await response.json().catch(() => null);
  if (!response.ok || result === null) {
    throw new Error(result?.detail || result?.erro || result?.message ||
      'Não foi possível concluir a solicitação. Verifique se o backend está rodando.');
  }
  return result;
}

export function useApi(path) {
  const [result, setResult] = useState(null);

  useEffect(() => {
    if (!path) return;
    const controller = new AbortController();
    apiRequest(path, { signal: controller.signal })
      .then(data => {
        if (!controller.signal.aborted) setResult({ path, data, error: '' });
      })
      .catch(error => {
        if (!controller.signal.aborted) setResult({ path, data: null, error: error.message });
      });
    return () => controller.abort();
  }, [path]);

  return result?.path === path
    ? { data: result.data, error: result.error, loading: false }
    : { data: null, error: '', loading: Boolean(path) };
}
