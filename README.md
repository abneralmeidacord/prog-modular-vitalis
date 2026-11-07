# Vitalis

Sistema de gestão hospitalar. O frontend foi migrado para React com Next.js e usa o Pages Router.

## Como executar

Com Node.js 20.9 ou superior, execute a partir da raiz do repositório:

```sh
cd frontend
npm ci
npm run dev
```

Abra [http://localhost:3000](http://localhost:3000). Para rodar em produção, execute `npm run build` e depois `npm start` dentro de `frontend`.

## Estrutura

```text
vitalis/
├── backend/                  # Reservado para a implementação do backend
├── docs/                     # Documentação acadêmica e técnica
└── frontend/
    ├── components/           # Uma pasta por componente, com .js e .module.css
    ├── data/                 # Dados de demonstração compartilhados
    ├── pages/                # Rotas do Pages Router
    ├── public/               # Arquivos estáticos
    └── styles/
        ├── globals.css       # Variáveis, estilos básicos e espaçamento das páginas
        └── PageContent.module.css
```

As telas exibem dados de demonstração. Os formulários aplicam a validação nativa do navegador e exibem um aviso ao enviar, pois o armazenamento depende da integração com o backend.
