import styles from './Avatar.module.css';

export default function Avatar({ children, tone = 'blue', size = 'normal' }) {
  return (
    <span className={styles.avatar} data-tone={tone} data-size={size} aria-hidden="true">
      {children}
    </span>
  );
}
