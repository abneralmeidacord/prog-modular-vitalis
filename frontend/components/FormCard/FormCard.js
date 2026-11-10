import { useRef, useState } from 'react';
import { useRouter } from 'next/router';
import Button from '../Button/Button';
import { apiRequest } from '../../lib/api';
import styles from './FormCard.module.css';

export default function FormCard({
  children, description, submitLabel, endpoint, destination, toPayload, disabled = false,
}) {
  const router = useRouter();
  const locked = useRef(false);
  const [pending, setPending] = useState(false);
  const [saved, setSaved] = useState(false);
  const [error, setError] = useState('');

  async function handleSubmit(event) {
    event.preventDefault();
    if (locked.current || disabled) return;

    locked.current = true;
    setPending(true);
    setError('');
    try {
      const fields = Object.fromEntries(new FormData(event.currentTarget));
      await apiRequest(endpoint, { method: 'POST', data: toPayload ? toPayload(fields) : fields });
      setSaved(true);
    } catch (error) {
      locked.current = false;
      setError(error.message);
      return;
    } finally {
      setPending(false);
    }

    await router.push(destination).catch(() => false);
  }

  return (
    <form className={'surface ' + styles.formCard} onSubmit={handleSubmit}>
      <div className={styles.formGrid}>{children}</div>
      <div className={styles.formFooter}>
        <span>{description}</span>
        <Button type="submit" disabled={disabled || pending || saved}>
          {pending ? 'Salvando…' : submitLabel}
        </Button>
      </div>
      {error && <p className={styles.notice} role="alert">{error}</p>}
      {saved && (
        <p className={styles.notice} role="status">
          Registro salvo. <Button href={destination} variant="ghost">Voltar à lista</Button>
        </p>
      )}
    </form>
  );
}
