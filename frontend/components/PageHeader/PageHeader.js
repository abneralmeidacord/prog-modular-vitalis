import Head from 'next/head';
import Button from '../Button/Button';
import styles from './PageHeader.module.css';

export default function PageHeader({ eyebrow, title, description, action, hero = false }) {
  return (
    <>
      <Head>
        <title>{`${title} | Vitalis`}</title>
      </Head>
      <header className={hero ? styles.hero : styles.sectionHead}>
        <div>
          <p className="eyebrow">{eyebrow}</p>
          <h1>{title}</h1>
          <p>{description}</p>
        </div>
        {action && <Button href={action.href} variant={action.variant}>{action.label}</Button>}
      </header>
    </>
  );
}
