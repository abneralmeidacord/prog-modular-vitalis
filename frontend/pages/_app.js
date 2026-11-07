import Head from 'next/head';
import Layout from '../components/Layout/Layout';
import '../styles/globals.css';

export default function App({ Component, pageProps }) {
  return (
    <>
      <Head>
        <title>Vitalis — Gestão Hospitalar</title>
        <meta name="description" content="Vitalis, sistema de gestão hospitalar." />
        <link rel="icon" href="/vitalis.svg" type="image/svg+xml" />
      </Head>
      <Layout>
        <Component {...pageProps} />
      </Layout>
    </>
  );
}
