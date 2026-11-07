import Avatar from '../Avatar/Avatar';
import Sidebar from '../Sidebar/Sidebar';
import styles from './Layout.module.css';

export default function Layout({ children }) {
  return (
    <div className={styles.appShell}>
      <Sidebar />
      <main className={styles.main}>
        <header className={styles.topbar}>
          <span>Hospital Santa Clara <b>/</b> Sistema de gestão</span>
          <span className={styles.account}>
            <Avatar>AC</Avatar> Administração　⌄
          </span>
        </header>
        <div className="page">
          {children}
          <footer className={styles.footer}>
            Vitalis · Sistema de Gestão Hospitalar
            <span>Dados protegidos e organizados para cuidar melhor.</span>
          </footer>
        </div>
      </main>
    </div>
  );
}
