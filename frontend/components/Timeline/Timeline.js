import styles from './Timeline.module.css';

export default function Timeline({ events }) {
  return (
    <div className={styles.timeline}>
      {events.map(({ title, date, description }) => (
        <p key={date}>
          <i aria-hidden="true" />
          <b>{title}</b>
          <small>{date}</small>
          <span>{description}</span>
        </p>
      ))}
    </div>
  );
}
