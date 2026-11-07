import Link from 'next/link';
import styles from './CardHeader.module.css';

export default function CardHeader({ title, description, link }) {
  return (
    <div className={styles.cardHead}>
      <div>
        <h2>{title}</h2>
        <p>{description}</p>
      </div>
      {link && <Link href={link.href}>{link.label}</Link>}
    </div>
  );
}
