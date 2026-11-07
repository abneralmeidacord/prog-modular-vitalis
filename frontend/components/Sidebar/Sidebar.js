import Link from 'next/link';
import { useRouter } from 'next/router';
import Avatar from '../Avatar/Avatar';
import styles from './Sidebar.module.css';

const groups = [
  {
    title: 'Visão geral',
    items: [{ href: '/', icon: '▦', label: 'Dashboard' }],
  },
  {
    title: 'Cadastros',
    items: [
      {
        href: '/pacientes',
        icon: '♙',
        label: 'Pacientes',
        count: 248,
        related: ['/novo-paciente', '/ficha-mariana', '/ficha-rafael'],
      },
      {
        href: '/profissionais',
        icon: '✚',
        label: 'Profissionais',
        count: 42,
        related: ['/novo-profissional'],
      },
      {
        href: '/quartos',
        icon: '□',
        label: 'Quartos',
        count: 36,
        related: ['/novo-quarto'],
      },
    ],
  },
  {
    title: 'Atendimentos',
    items: [
      {
        href: '/consultas',
        icon: '◷',
        label: 'Consultas',
        count: 18,
        related: ['/nova-consulta'],
      },
      {
        href: '/internacoes',
        icon: '⌂',
        label: 'Internações',
        count: 12,
        related: ['/nova-internacao'],
      },
      { href: '/historico', icon: '≡', label: 'Histórico médico' },
    ],
  },
];

export default function Sidebar() {
  const { pathname } = useRouter();

  return (
    <aside className={styles.sidebar}>
      <Link href="/" className={styles.brand}>
        <span className={styles.brandMark} aria-hidden="true">+</span>
        <span>
          <b>Vitalis</b>
          <small>Gestão hospitalar</small>
        </span>
      </Link>
      <div className={styles.profile}>
        <Avatar>AC</Avatar>
        <span>
          <b>Administração</b>
          <small>Hospital Santa Clara</small>
        </span>
        <i aria-hidden="true" />
      </div>
      <nav aria-label="Navegação principal">
        {groups.map(({ title, items }) => (
          <div className={styles.navGroup} key={title}>
            <small className={styles.navTitle}>{title}</small>
            {items.map(({ href, icon, label, count, related }) => {
              const current = pathname === href;
              const active = current || related?.includes(pathname);

              return (
                <Link
                  key={href}
                  href={href}
                  className={styles.navLink}
                  data-active={active || undefined}
                  aria-current={current ? 'page' : undefined}
                >
                  <span aria-hidden="true">{icon}</span>
                  <span>{label}</span>
                  {count && <em>{count}</em>}
                </Link>
              );
            })}
          </div>
        ))}
      </nav>
      <div className={styles.sidebarFooter}>
        <Link href="/regras">? &nbsp; Central de ajuda</Link>
        <Link href="/regras">⚙ &nbsp; Regras do sistema</Link>
      </div>
    </aside>
  );
}
