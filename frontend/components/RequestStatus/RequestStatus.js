export default function RequestStatus({ request, empty }) {
  if (request.loading) return <p role="status">Carregando…</p>;
  if (request.error) return <p role="alert">{request.error}</p>;
  if (empty && !request.data?.length) return <p>{empty}</p>;
  return null;
}
