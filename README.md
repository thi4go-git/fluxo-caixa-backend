# Fluxo Caixa Backend (Quarkus)

Backend da aplicação de fluxo de caixa, construído com Quarkus, JPA/Hibernate e PostgreSQL.

## Resumo do projeto

Este serviço expõe APIs para:
- usuários (`/usuarios`);
- naturezas de lançamento (`/naturezas`);
- lançamentos financeiros, anexos e dashboard (`/lancamentos`);
- recebimento de webhook da Pluggy (`/pluggy/webhooks`);
- geração de token de dashboard do Metabase (`/metabase/dashboard-url-token`).

A documentação da API fica disponível via Swagger em `/docs`.

## Versões de Java e Maven

- Java mínimo: **11** (definido por `maven.compiler.release=11` no `pom.xml`).
- Java recomendado: **17 LTS** (alinhado com a imagem Docker JVM usada no projeto).
- Maven: **3.8.6** (definido no Maven Wrapper em `.mvn/wrapper/maven-wrapper.properties`).

Recomendação: usar sempre o wrapper (`mvnw`/`mvnw.cmd`) para evitar divergência de versão do Maven.

## Variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto com as variáveis abaixo:

```env
SERVER_BANCO_USUARIO=
SERVER_BANCO_SENHA=
SERVER_BANCO_IP_PORT=
SERVER_BANCO_BASE_NAME=

KEYCLOAK_SERVER=
KEYCLOAK_REALM=
KEYCLOAK_CLI_ID=
KEYCLOAK_CLI_SECRET=

PLUGGY_URL=
PLUGGY_SECRET_WEBHOOK=
PLUGGY_CLIENT_ID=
PLUGGY_CLIENT_SECRET=
PLUGGY_ID_CONTA=
PLUGGY_ITEM_ID_CONTA=

METABASE_INSTANCE_URL=
METABASE_SECRET_KEY=
METABASE_DASHBOARD_ID=

VERSION=1.0.0
```

## Como rodar localmente

1. Garanta Java 11+ instalado (preferencialmente Java 17).
2. Configure o `.env`.
3. Rode em modo de desenvolvimento:

Linux/macOS:
```bash
./mvnw compile quarkus:dev
```

Windows (PowerShell):
```powershell
.\mvnw.cmd compile quarkus:dev
```

Endpoints úteis:
- API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/docs`

## Como rodar usando Docker

O `docker-compose.yml` usa o `src/main/docker/Dockerfile.jvm`, que espera o pacote já gerado em `target/quarkus-app`.

1. Gerar o build:

Linux/macOS:
```bash
./mvnw clean package
```

Windows (PowerShell):
```powershell
.\mvnw.cmd clean package
```

2. Subir os containers:
```bash
docker compose up --build -d
```

A aplicação ficará disponível em:
- `http://localhost:8001`
- Swagger UI: `http://localhost:8001/docs`

## Como rodar os testes unitários

Os testes estão em `src/test` com `@QuarkusTest`, usando perfil `%test` com banco H2 em memória.

Linux/macOS:
```bash
./mvnw test
```

Windows (PowerShell):
```powershell
.\mvnw.cmd test
```
