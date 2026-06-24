# URL Shortener

Encurtador de URLs com Spring Boot, Redis e PostgreSQL. Gera códigos aleatórios, redireciona para o destino original e conta acessos em tempo real.

## Funcionalidades

- Criação de URLs curtas com código aleatório (7 caracteres)
- Redirecionamento 302 para a URL original
- Contador de cliques em tempo real (Redis)
- Expiração automática de links (configurável em dias)
- Estatísticas individuais por link
- Documentação Swagger UI automática
- Cache distribuído com Redis

## Tecnologias

- **Java 17**
- **Spring Boot 3.5.4** (Web, JPA, Security, Validation, Redis, Cache)
- **PostgreSQL 16**
- **Redis 7** (cache e contador de cliques)
- **Flyway** (migrações de banco)
- **SpringDoc OpenAPI** (Swagger)
- **Docker Compose**
- **Lombok**

## Pré-requisitos

- Java 17+
- Docker Desktop (para PostgreSQL e Redis)

## Estrutura do Projeto

```
shortener/
├── src/main/java/com/br/url/shortener/shortener/
│   ├── config/
│   │   ├── OpenApiConfig.java
│   │   └── RedisConfig.java
│   ├── controller/
│   │   └── UrlController.java
│   ├── database/
│   │   ├── entity/
│   │   │   └── ShortUrl.java
│   │   └── repository/
│   │       └── ShortUrlRepository.java
│   ├── dto/
│   │   ├── request/
│   │   │   └── CreateUrlRequest.java
│   │   └── response/
│   │       ├── ErrorResponse.java
│   │       └── UrlResponse.java
│   ├── exception/
│   │   ├── custom/
│   │   │   ├── LinkExpiredException.java
│   │   │   └── ShortUrlNotFoundException.java
│   │   └── handler/
│   │       └── GlobalExceptionHandler.java
│   ├── service/
│   │   └── UrlService.java
│   └── util/
│       └── ShortCodeGenerator.java
├── src/main/resources/
│   ├── application.yaml
│   └── db/migration/
├── docker-compose.yml
├── build.gradle
└── settings.gradle
```

## Como Executar

### 1. Suba os containers

```bash
cd shortener
docker compose up -d
```

### 2. Execute a aplicação

```bash
./gradlew bootRun
```

A aplicação iniciará em `http://localhost:8080`.

## Endpoints da API

| Método | Rota                          | Descrição                          |
|--------|-------------------------------|------------------------------------|
| POST   | `/api/urls`                   | Criar um link encurtado            |
| GET    | `/{shortCode}`                | Redirecionar para URL original     |
| GET    | `/api/urls`                   | Listar todos os links              |
| GET    | `/api/urls/{shortCode}/stats` | Estatísticas de um link            |
| DELETE | `/api/urls/{shortCode}`       | Remover um link                    |

### Exemplo: Criar URL

```bash
curl -X POST http://localhost:8080/api/urls \
  -H "Content-Type: application/json" \
  -d '{"originalUrl": "https://exemplo.com", "expiresInDays": 30}'
```

## Documentação Swagger

Acesse `http://localhost:8080/swagger-ui.html` após iniciar a aplicação.

## Banco de Dados

- PostgreSQL: `localhost:5433` (usuário: `postgres`, senha: `root`, database: `urlshortener`)
- Redis: `localhost:6379`
