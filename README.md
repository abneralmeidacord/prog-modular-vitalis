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

### API de profissionais

Com Java 17+ e Maven 3.6.3+, execute a partir da raiz:

```sh
mvn -f backend/pom.xml test
mvn -f backend/pom.xml spring-boot:run
```

A API roda em `http://localhost:8080` e mantém os cadastros em memória enquanto o servidor estiver ativo.

- `POST /profissionais` cadastra `CLINICO_GERAL`, `ESPECIALISTA` ou `CIRURGIAO`.
- `GET /profissionais` lista todos os cadastrados.
- `GET /profissionais?area=Cardiologia` lista os profissionais disponíveis com área correspondente, ignorando maiúsculas, minúsculas e espaços nas pontas.
- `GET /profissionais/{id}` busca um cadastro.

Exemplo de corpo JSON para `POST /profissionais`:

```json
{
  "categoria": "ESPECIALISTA",
  "nome": "Helena Martins",
  "registro": "CRM 48291",
  "telefone": "(31) 3333-1234",
  "email": "helena@example.com",
  "especialidade": "Cardiologia"
}
```

`ESPECIALISTA` exige `especialidade`, `CIRURGIAO` exige `areaCirurgica` e `CLINICO_GERAL` usa a área `Clínica geral`. O campo `disponivel` é opcional e assume `true`.

Para a integração da Sprint 2, `AtendimentoClinico` representa consulta de rotina, avaliação inicial e encaminhamento. `Cirurgiao` fornece `VinculoCirurgico` com o profissional, o `pacienteId` (`long`, conforme o UML), data, horário e descrição. A issue #3 define o procedimento completo e seu ciclo de vida.

## Estrutura

```text
vitalis/
├── backend/                  # API Spring Boot
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
