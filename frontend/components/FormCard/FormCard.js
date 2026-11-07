import { useState } from 'react';
import Button from '../Button/Button';
import styles from './FormCard.module.css';

export default function FormCard({ children, description, submitLabel }) {
  const [submitted, setSubmitted] = useState(false);

  function handleSubmit(event) {
    event.preventDefault();
    setSubmitted(true);
  }

  return (
    <form className={`surface ${styles.formCard}`} onSubmit={handleSubmit}>
      <div className={styles.formGrid}>{children}</div>
      <div className={styles.formFooter}>
        <span>{description}</span>
        <Button type="submit">{submitLabel}</Button>
      </div>
      {submitted && (
        <p className={styles.notice} role="status">
          Formulário de demonstração. Os dados serão salvos quando o backend for integrado.
        </p>
      )}
    </form>
  );
}
