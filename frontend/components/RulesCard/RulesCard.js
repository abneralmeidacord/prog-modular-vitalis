import styles from './RulesCard.module.css';

export default function RulesCard({ title, description, items, icon = 'i', compact, as: Tag = 'aside' }) {
  return (
    <Tag className={`surface ${styles.rules}`} data-compact={compact || undefined}>
      <h3><span aria-hidden="true">{icon}</span> {title}</h3>
      {description && <p>{description}</p>}
      {items && (
        <ul>
          {items.map((item) => <li key={item}>{item}</li>)}
        </ul>
      )}
    </Tag>
  );
}
