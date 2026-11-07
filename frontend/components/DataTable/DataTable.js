import styles from './DataTable.module.css';

export default function DataTable({ toolbar, summary, children }) {
  return (
    <article className="surface">
      {toolbar && <div className={styles.toolbar}>{toolbar}</div>}
      {summary && <div className={styles.roomSummary}>{summary}</div>}
      <div className={styles.tableCard}>
        <table>{children}</table>
      </div>
    </article>
  );
}
