export default {
  async redirects() {
    return ['/ficha-mariana', '/ficha-rafael'].map(source => ({
      source, destination: '/pacientes', permanent: false,
    }));
  },
};
