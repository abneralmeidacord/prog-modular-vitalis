import RequestStatus from '../RequestStatus/RequestStatus';
import styles from './DataTable.module.css';

export default function DataTable({ toolbar, summary, children, request, empty, colSpan }) {
  return (
    <article className="surface">
      {toolbar && <div className={styles.toolbar}>{toolbar}</div>}
      {summary && <div className={styles.roomSummary}>{summary}</div>}
      <div className={styles.tableCard}>
        <table>
          {children}
          {request && (request.loading || request.error || (empty && !request.data?.length)) && (
            <tbody>
              <tr><td colSpan={colSpan}><RequestStatus request={request} empty={empty} /></td></tr>
            </tbody>
          )}
        </table>
      </div>
    </article>
  );
}
