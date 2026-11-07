import styles from './PatientCard.module.css';

export default function PatientCard({ children, as: Tag = 'article' }) {
  const className = Tag === 'article' ? `surface ${styles.patientCard}` : styles.patientCard;

  return <Tag className={className}>{children}</Tag>;
}
