# BookingMaster - Arquitetura e Documentação

## Visão Geral

BookingMaster é uma API REST para gerenciamento de reservas de hotéis, desenvolvida com Spring Boot 3.2.1 e Java 21.

---

## Arquitetura do Projeto

```
bookingmaster/
├── src/
│   ├── main/
│   │   ├── java/com/gramos22/bookingmaster/
│   │   │   ├── controllers/     # REST Controllers
│   │   │   ├── dtos/            # Data Transfer Objects
│   │   │   ├── entities/        # Entidades JPA
│   │   │   ├── repositories/    # Interfaces JPA Repository
│   │   │   ├── services/        # Lógica de negócio
│   │   │   └── utils/           # Utilitários
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-prod.properties
│   │       └── application-test.properties
│   └── test/
│       └── java/com/gramos22/bookingmaster/
│           ├── integration/     # Testes de integração
│           └── acceptance/      # Testes de aceitação E2E
├── docker-compose.yml           # Infraestrutura completa
├── docker-compose-test.yml      # Ambiente de testes E2E
├── Dockerfile                   # Build da aplicação
├── Dockerfile.jenkins           # Jenkins customizado
└── Jenkinsfile                  # Pipeline CI/CD
```

---

## Endpoints da API

**Documentação interativa disponível em:** http://localhost:8080/swagger-ui.html

| Recurso   | Método | Endpoint        | Descrição                |
|-----------|--------|-----------------|--------------------------|
| Swagger   | GET    | /               | Redireciona para Swagger UI |
| Swagger   | GET    | /swagger-ui.html| Interface Swagger UI     |
| Swagger   | GET    | /api-docs       | OpenAPI JSON             |
| Hotels    | GET    | /hotels         | Lista todos os hotéis    |
| Hotels    | GET    | /hotels/{id}    | Busca hotel por ID       |
| Hotels    | POST   | /hotels         | Cria novo hotel          |
| Hotels    | DELETE | /hotels/{id}    | Remove hotel             |
| Users     | GET    | /users/{id}     | Busca usuário por ID     |
| Users     | POST   | /users          | Cria novo usuário        |
| Users     | DELETE | /users/{id}     | Remove usuário           |
| Bookings  | GET    | /bookings       | Lista todas as reservas  |
| Bookings  | GET    | /bookings/{id}  | Busca reserva por ID     |
| Bookings  | POST   | /bookings       | Cria nova reserva        |
| Bookings  | DELETE | /bookings/{id}  | Cancela reserva          |
| Health    | GET    | /actuator/health| Health check             |

---

## Infraestrutura Docker

### Containers em Produção

| Container           | Imagem                  | Porta        | Descrição                    |
|---------------------|-------------------------|--------------|------------------------------|
| `bookingmaster-api` | bookingmaster:latest    | 8080         | API REST (produção)          |
| `bookingmaster-db`  | mariadb:11.2            | 3306         | Banco de dados MariaDB       |
| `jenkins`           | bookingmaster-jenkins   | 8081, 50000  | Servidor CI/CD               |
| `ngrok`             | ngrok/ngrok:latest      | 4040         | Túnel para expor a API       |

### Exposição da Aplicação

```
┌─────────────────────────────────────────────────────────────┐
│                        INTERNET                              │
│                            │                                 │
│                      ┌─────▼─────┐                          │
│                      │   NGROK   │                          │
│                      │  (túnel)  │                          │
│                      └─────┬─────┘                          │
│                            │                                 │
├────────────────────────────┼────────────────────────────────┤
│              REDE LOCAL (bookingmaster-network)              │
│                            │                                 │
│    ┌───────────────────────┼───────────────────────┐        │
│    │                       │                       │        │
│    ▼                       ▼                       ▼        │
│ ┌──────┐            ┌──────────────┐         ┌─────────┐   │
│ │ DB   │◄───────────│     API      │         │ Jenkins │   │
│ │:3306 │            │    :8080     │         │  :8081  │   │
│ └──────┘            └──────────────┘         └─────────┘   │
│                                                   │         │
│                                              (local only)   │
└─────────────────────────────────────────────────────────────┘
```

**Acessos:**
- **API Pública:** URL do ngrok (ver http://localhost:4040)
- **API Local:** http://localhost:8080
- **Jenkins:** http://localhost:8081 (somente local)
- **Ngrok Dashboard:** http://localhost:4040 (somente local)

---

## Pipeline CI/CD (Jenkins)

### Fluxo do Pipeline

```
┌─────────────────────────────────────────────────────────────────┐
│                      COMMIT STAGE                                │
│  ┌──────────┐  ┌───────┐  ┌────────────┐  ┌─────────────────┐   │
│  │ Checkout │─▶│ Build │─▶│ Unit Tests │─▶│Integration Tests│   │
│  └──────────┘  └───────┘  └────────────┘  └────────┬────────┘   │
│                                                     │            │
│  ┌─────────────────────┐  ┌──────────────────────┐ │            │
│  │ Build Docker Image  │◀─│      Package         │◀┘            │
│  └──────────┬──────────┘  └──────────────────────┘              │
├─────────────┼───────────────────────────────────────────────────┤
│             ▼          ACCEPTANCE STAGE                          │
│  ┌─────────────────────┐  ┌──────────────────────┐              │
│  │ Start Test Environment│─▶│ Acceptance Tests (E2E)│            │
│  │  (docker-compose)    │  │   (REST Assured)      │            │
│  └─────────────────────┘  └──────────┬───────────┘              │
│                                       │                          │
│  ┌─────────────────────┐              │                          │
│  │ Cleanup Environment │◀─────────────┘                          │
│  └──────────┬──────────┘                                         │
├─────────────┼───────────────────────────────────────────────────┤
│             ▼          RELEASE STAGE                             │
│  ┌─────────────────────┐  ┌──────────────────────┐              │
│  │ Deploy to Production │─▶│ Production Health    │              │
│  │ (docker run)         │  │ Check                │              │
│  └─────────────────────┘  └──────────┬───────────┘              │
│                                       │                          │
│  ┌─────────────────────┐              │                          │
│  │    Tag Release      │◀─────────────┘                          │
│  └─────────────────────┘                                         │
└─────────────────────────────────────────────────────────────────┘
```

### Configuração do Jenkins

1. **Acesse:** http://localhost:8081
2. **Configure o job:**
   - Tipo: Pipeline
   - SCM: Git (URL do repositório)
   - Script: Pipeline from SCM → Jenkinsfile
3. **Webhook (opcional):**
   - Use a URL do ngrok + `/github-webhook/` para trigger automático

### Tipos de Testes

| Tipo        | Comando Local                               | Banco              |
|-------------|---------------------------------------------|--------------------|
| Unitários   | `./mvnw test`                               | MariaDB (container)|
| Integração  | `./mvnw verify` (requer docker-compose-test)| MariaDB (container)|
| Aceitação   | `./mvnw verify -DskipAcceptanceTests=false` | MariaDB (container)|

---

## Profiles Spring

| Profile            | Uso                    | Banco     | DDL-Auto     |
|--------------------|------------------------|-----------|--------------|
| (default)          | Desenvolvimento local  | MariaDB   | update       |
| `prod`             | Produção               | MariaDB   | update       |
| `test`             | Testes de aceitação    | MariaDB   | create-drop  |
| `integration-test` | Testes de integração   | MariaDB   | create-drop  |

---

## Como Executar

### Iniciar Infraestrutura Completa

```bash
docker compose up -d
```

### Executar Testes Localmente

```bash
# Banco de teste (necessário para todos os testes)
docker compose -f docker-compose-test.yml up -d mariadb-test

# Testes unitários + integração
./mvnw verify

# Apenas testes unitários
./mvnw test
```

### Build da Imagem Docker

```bash
docker build -t bookingmaster:latest .
```

---

## Dependências Principais

| Dependência           | Versão  | Propósito                |
|-----------------------|---------|--------------------------|
| Spring Boot           | 3.2.1   | Framework base           |
| Spring Data JPA       | 3.2.1   | Persistência             |
| MariaDB JDBC          | 3.3.2   | Driver de banco          |
| REST Assured          | 5.4.0   | Testes de aceitação      |
| Lombok                | 1.18.30 | Redução de boilerplate   |

---

## Gerenciamento de Secrets

### Decisão Técnica

Todas as credenciais e informações sensíveis foram removidas do código-fonte e movidas para:
- **Jenkins Credentials** (CI/CD)
- **Variáveis de ambiente** (runtime)
- **Arquivo `.env`** (apenas desenvolvimento local - não versionado)

### Por que essa abordagem?

1. **Segurança:** Nenhuma credencial hardcoded no repositório Git
2. **Flexibilidade:** Valores diferentes para dev/test/prod sem alterar código
3. **Conformidade:** Alinhado com as melhores práticas de DevSecOps
4. **Auditoria:** Credenciais centralizadas e rastreáveis no Jenkins

### Secrets do Jenkins (CI/CD)

| Secret ID             | Descrição                              | Usado em            |
|-----------------------|----------------------------------------|---------------------|
| `PROD_DB_URL`         | URL JDBC do banco de produção          | Release Stage       |
| `PROD_DB_USER`        | Usuário do banco de produção           | Release Stage       |
| `PROD_DB_PASSWORD`    | Senha do banco de produção             | Release Stage       |
| `TEST_DB_ROOT_PASSWORD`| Senha root do MariaDB de teste         | Commit/Acceptance   |
| `TEST_DB_NAME`        | Nome do banco de teste                 | Commit/Acceptance   |
| `TEST_DB_USER`        | Usuário do banco de teste              | Commit/Acceptance   |
| `TEST_DB_PASSWORD`    | Senha do banco de teste                | Commit/Acceptance   |

### Variáveis do Docker Compose (arquivo .env)

| Variável          | Descrição                              |
|-------------------|----------------------------------------|
| `NGROK_AUTHTOKEN` | Token de autenticação do Ngrok         |
| `DB_ROOT_PASSWORD`| Senha root do MariaDB de produção      |
| `DB_NAME`         | Nome do banco de dados de produção     |
| `DB_USER`         | Usuário do banco de produção           |
| `DB_PASSWORD`     | Senha do usuário do banco de produção  |

### Configuração Local

Para rodar localmente, crie um arquivo `.env` na raiz do projeto:

```bash
# Copie o template
cp .env.example .env

# Edite com seus valores
nano .env
```

**IMPORTANTE:** O arquivo `.env` não deve ser versionado (já está no `.gitignore`).

---

## Histórico de Mudanças

### [2024-XX-XX] Remoção de Credenciais Hardcoded

**O que mudou:**
- Removidos todos os valores padrão de senhas e credenciais dos arquivos de configuração
- `docker-compose.yml`, `docker-compose-test.yml`, `docker-compose.deploy.yml` agora usam apenas variáveis de ambiente
- `Dockerfile` não define mais DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD como ENV defaults
- Arquivos `application-*.properties` não têm mais fallback de credenciais

**Por que mudou:**
- Boas práticas de segurança: credenciais não devem estar no código-fonte
- Compliance com padrões de DevSecOps
- Preparação para deploy em ambientes de produção reais

**Impacto:**
- Jenkinsfile atualizado para injetar secrets via `credentials()`
- `.env` agora é obrigatório para execução local do docker-compose
- Testes requerem que as variáveis de ambiente estejam configuradas
