const resources = new Set(['pacientes', 'profissionais', 'quartos', 'consultas', 'internacoes']);

export default async function vitalisApi(req, res) {
  const [resource, ...segments] = req.query.path;

  if (!resources.has(resource) || segments.some(segment => !/^[a-z0-9-]+$/.test(segment))) {
    return res.status(404).json({ detail: 'Recurso não encontrado.' });
  }

  const { method } = req;
  if (method !== 'GET' && method !== 'POST') {
    res.setHeader('Allow', 'GET, POST');
    return res.status(405).json({ detail: 'Método não permitido.' });
  }

  const backend = (process.env.VITALIS_API_URL || 'http://127.0.0.1:8080').replace(/\/+$/, '');
  const query = new URL(req.url, 'http://localhost').search;
  const endpoint = backend + '/' + [resource, ...segments].join('/') + query;

  try {
    const upstream = await fetch(endpoint, {
      method,
      ...(method === 'POST' ? {
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(req.body),
      } : {}),
      signal: AbortSignal.timeout(15000),
    });
    const body = await upstream.text();
    const contentType = upstream.headers.get('content-type');
    const location = upstream.headers.get('location');

    if (contentType) res.setHeader('Content-Type', contentType);
    if (location?.startsWith('/' + resource + '/')) res.setHeader('Location', '/api/vitalis' + location);
    return res.status(upstream.status).send(body);
  } catch {
    return res.status(502).json({ detail: 'Não foi possível conectar ao backend do Vitalis.' });
  }
}
