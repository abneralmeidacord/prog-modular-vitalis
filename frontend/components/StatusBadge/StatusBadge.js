import styles from './StatusBadge.module.css';

export default function StatusBadge({ children, tone }) {
  return <span className={`${styles.badge} ${styles[tone]}`} data-status={tone}>{children}</span>;
}
